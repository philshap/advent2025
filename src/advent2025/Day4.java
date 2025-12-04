package advent2025;

import java.util.Set;
import java.util.stream.Collectors;

public class Day4 extends Day {
  public Day4() {
    super(4);
  }

  long countNeighbors(Pos roll, Set<Pos> rolls) {
    return Pos.EDGES.stream().map(roll::plus).filter(rolls::contains).count();
  }

  private long countMoveable(Set<Pos> rolls) {
    return rolls.stream().filter(roll -> countNeighbors(roll, rolls) < 4).count();
  }

  private Set<Pos> readRolls() {
    return Pos.streamOf(input).filter(pair -> pair.r() == '@').map(Pair::l).collect(Collectors.toSet());
  }

  @Override
  String part1() {
    Set<Pos> rolls = readRolls();
    long count = countMoveable(rolls);
    return String.valueOf(count);
  }

  Set<Pos> removeRolls(Set<Pos> rolls) {
    return rolls.stream().filter(roll -> countNeighbors(roll, rolls) >= 4).collect(Collectors.toSet());
  }

  @Override
  String part2() {
    Set<Pos> rolls = readRolls();
    int origCount = rolls.size();
    int prevCount;
    do {
      prevCount = rolls.size();
      rolls = removeRolls(rolls);
    } while (rolls.size() != prevCount);
    return String.valueOf(origCount - rolls.size());
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
