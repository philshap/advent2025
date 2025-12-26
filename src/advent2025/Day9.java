package advent2025;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 extends Day {
  Day9() {
    super(9);
  }

  long area(List<Pos> pair) {
    Pos diff = pair.getFirst().minus(pair.getLast());
    return (Math.abs(diff.x()) + 1L) * (Math.abs(diff.y()) + 1);
  }

  @Override
  String part1() {
    List<Pos> tiles = Support.partition(Support.integers(data), 2).map(Pos::new).toList();
    long maxArea = Combinations.combinations(tiles, 2)
        .mapToLong(this::area)
        .max().orElseThrow();
    return String.valueOf(maxArea);
  }

  record Line(Pos min, Pos max) {
    Line(List<Pos> pair) {
      // Store endpoints in canonical form
      Pos p1 = pair.getFirst();
      Pos p2 = pair.getLast();
      this(new Pos(Math.min(p1.x(), p2.x()), Math.min(p1.y(), p2.y())),
          new Pos(Math.max(p1.x(), p2.x()), Math.max(p1.y(), p2.y())));
    }

    boolean isVertical() {
      return min.x() == max.x();
    }

    // Does this line cross any edge of a box?
    boolean crossBox(Line box) {
      if (isVertical()) {
        return box.min.x() < min.x() && min.x() < box.max.x() && box.min.y() < max.y() && min.y() < box.max.y();
      }
      return box.min.y() < min.y() && min.y() < box.max.y() && box.min.x() < max.x() && min.x() < box.max.x();
    }

    // Return the points inside each of the four corners
    Stream<Pos> insideCorners() {
      // Not correct for flat boxes, assume the largest box isn't flat.
      return Stream.of(new Pos(min.x() + 1, min.y() + 1),
          new Pos(min.x() + 1, max.y() - 1),
          new Pos(max.x() - 1, min.y() + 1),
          new Pos(max.x() - 1, max.y() - 1));
    }
  }

  // Check if a box is inside a perimeter. To limit the number of points checked,
  // - check each point inside each of the four box corners
  // - then check to see if any box edge is crossed by a perimeter line
  record Perimeter(List<Line> lines) {
    // A point is inside the perimeter if there's an odd number of line crossings between
    // it and a point outside the perimeter.
    boolean isInside(Pos pos) {
      long crossings = lines.stream()
          .filter(line -> line.isVertical() && line.min.x() >= pos.x()
              && line.min.y() <= pos.y() && pos.y() < line.max.y())
          .count();

      return crossings % 2 == 1;
    }

    boolean noEdgeCrossings(Line box) {
      return lines.stream().noneMatch(line -> line.crossBox(box));
    }

    boolean inside(List<Pos> box) {
      // Line is useful for min/max APIs.
      Line boxLine = new Line(box);
      return boxLine.insideCorners().allMatch(this::isInside) && noEdgeCrossings(boxLine);
    }
  }

  @Override
  String part2() {
    List<Pos> tiles = Support.partition(Support.integers(data), 2).map(Pos::new).toList();
    List<Line> allLines = Support.partition(tiles, 2, 1)
        .map(Line::new)
        .collect(Collectors.toList());
    allLines.add(new Line(List.of(tiles.getLast(), tiles.getFirst())));
    Perimeter perimeter = new Perimeter(allLines);

    long maxArea = Combinations.combinations(tiles, 2)
        .sorted(Comparator.comparingLong(this::area).reversed())
        .filter(perimeter::inside)
        .map(this::area)
        .findFirst().orElseThrow();
    return String.valueOf(maxArea);
  }

  static void main() {
    Day day = new Day9() {
      @Override
      String getData() {
        return """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""";
      }
    };
    day.run("50", "24");
  }
}
