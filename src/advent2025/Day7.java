package advent2025;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 extends Day {
  public Day7() {
    super(7);
  }

  static final HashMap<Pos, Long> CACHE = new HashMap<>();

  record Diagram(Set<Pos> splitters, Pos start, int height) {
    Diagram(List<String> input) {
      this(Pos.streamOf(input)
          .filter(pair -> pair.r() == '^')
          .map(Pair::l).collect(Collectors.toSet()),
          new Pos(input.getFirst().indexOf('S'), 0),
          input.size());
    }

    Set<Pos> split(Pos pos) {
      if (splitters.contains(pos)) {
        return Set.of((pos.plus(Pos.L)), (pos.plus(Pos.R)));
      }
      return Set.of(pos);
    }

    long memoCountSplits(Pos beam) {
      return Support.computeIfAbsent(CACHE, beam, this::countSplits);
    }

    long countSplits(Pos beam) {
      Pos next = beam.plus(Pos.D);
      if (next.y() == height) {
        return 0;
      }
      if (splitters.contains(next)) {
        return 1 + memoCountSplits(beam.plus(Pos.L)) + memoCountSplits(beam.plus(Pos.R));
      }
      return memoCountSplits(next);
    }
  }

  @Override
  String part1() {
    Diagram diagram = new Diagram(input);
    Set<Pos> beams = Set.of(diagram.start);
    int[] splits = {0};
    for (int i = 0; i < diagram.height; i++) {
      beams = beams.stream().map(Pos.D::plus).flatMap(pos -> {
        var split = diagram.split(pos);
        if (split.size() == 2) {
          splits[0]++;
        }
        return split.stream();
      }).collect(Collectors.toSet());
    }
    return String.valueOf(splits[0]);
  }

  @Override
  String part2() {
    Diagram diagram = new Diagram(input);
    return String.valueOf(diagram.countSplits(diagram.start) + 1);
  }

  static void main() {
    Day day = new Day7() {
      @Override
      String getData() {
        return """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............""";
      }
    };
    day.run("21", "40");
  }
}
