package advent2025;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 extends Day {
  public Day4() {
    super(4);
  }

  record Grid(Set<Pos> rolls) {
    Grid(List<String> input) {
      this(Pos.streamOf(input).filter(pair -> pair.r() == '@').map(Pair::l).collect(Collectors.toSet()));
    }
    long countNeighbors(Pos roll) {
      return Pos.EDGES.stream().map(roll::plus).filter(rolls::contains).count();
    }

    long countMoveable() {
      return rolls.stream().filter(roll -> countNeighbors(roll) < 4).count();
    }
    Grid removeRolls() {
      return new Grid(rolls.stream().filter(roll -> countNeighbors(roll) >= 4).collect(Collectors.toSet()));
    }
  }

  @Override
  String part1() {
    return String.valueOf(new Grid(input).countMoveable());
  }

  @Override
  String part2() {
    Grid grid = new Grid(input);
    Grid allMoved =
        Stream.iterate(Pair.of(grid, grid.removeRolls()), pair -> Pair.of(pair.r(), pair.r().removeRolls()))
        .dropWhile(pair -> !pair.l().equals(pair.r()))
        .findFirst().orElseThrow().l();
    return String.valueOf(grid.rolls.size() - allMoved.rolls.size());
  }

  static void main() {
    Day day = new Day4() {
      @Override
      String getData() {
        return """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.""";
      }
    };
    day.run("13", "43");
  }
}
