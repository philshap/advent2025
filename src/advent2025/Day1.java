package advent2025;

public class Day1 extends Day {
  protected Day1() {
    super(1);
  }

  String part1() {
    return "0";
  }

  String part2() {
    return "0";
  }

  public static void main(String[] args) {
    Day day = new Day1() {
      @Override
      String getData() {
        return "";
      }
    };
    day.run("0", "0");
  }
}
