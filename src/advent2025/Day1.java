package advent2025;

public class Day1 extends Day {
  protected Day1() {
    super(1);
  }

  static int lineToOffset(String line) {
    return (line.charAt(0) == 'R' ? 1 : -1) * Integer.parseInt(line.substring(1));
  }

  String part1() {
    int dial = 50;
    int numZeros = 0;
    for (String line : input) {
      dial += lineToOffset(line);
      if (dial > 99) {
        dial %= 100;
      } else if (dial < 0) {
        dial = 100 - (-dial % 100);
      }
      if (dial == 0 || dial == 100) {
        numZeros++;
      }
    }
    return String.valueOf(numZeros);
  }

  String part2() {
    int dial = 50;
    int numZeros = 0;
    for (String line : input) {
      int offset = lineToOffset(line);
      if (offset > 0) {
        dial += offset;
        numZeros += dial / 100;
        dial %= 100;
      } else {
        while (offset++ != 0) {
          if (--dial == 0) {
            numZeros++;
          }
          if (dial == -1) {
            dial = 99;
          }
        }
      }
    }
    return String.valueOf(numZeros);
  }

  public static void main(String[] args) {
    Day day = new Day1() {
      @Override
      String getData() {
        return """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82""";
      }
    };
    day.run("3", "6");
  }
}
