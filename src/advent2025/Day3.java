package advent2025;

import java.util.Comparator;
import java.util.stream.IntStream;

public class Day3 extends Day {
  public Day3() {
    super(3);
  }

  record Battery(int pos, int jolt) {}

  long maxJoltage(String bank, int digits) {
    long joltage = 0;
    int offset = 0;
    for (int i = digits - 1; i >= 0; i--) {
      Battery battery = IntStream.range(offset, bank.length() - i)
          .mapToObj(pos -> new Battery(pos, bank.charAt(pos) - '0'))
          .max(Comparator.comparing(Battery::jolt)).orElseThrow();
      joltage = battery.jolt + joltage * 10;
      offset = battery.pos + 1;
    }
    return joltage;
  }

  @Override
  String part1() {
    long total = input.stream().mapToLong(bank -> maxJoltage(bank, 2)).sum();
    return String.valueOf(total);
  }

  @Override
  String part2() {
    long total = input.stream().mapToLong(bank -> maxJoltage(bank, 12)).sum();
    return String.valueOf(total);
  }

  static void main() {
    Day day = new Day3() {
      @Override
      String getData() {
        return """
            987654321111111
            811111111111119
            234234234234278
            818181911112111""";
      }
    };
    day.run("357", "3121910778619");
  }
}
