package advent2025;

import java.util.List;

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

  // Filter list of boxes to ones that are fully inside the perimeter.
  // A point is inside the perimeter if there's an odd number of line crossings between
  // it and a point outside the perimeter.

  @Override
  String part2() {
    List<Pos> tiles = Support.partition(Support.integers(data), 2).map(Pos::new).toList();
    long maxArea = Combinations.combinations(tiles, 2)
        .filter(x -> true)
        .mapToLong(this::area)
        .max().orElseThrow();
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
    day.run(null, null);
  }
}
