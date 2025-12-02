package advent2025;

import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class Day2 extends Day {

  protected Day2() {
    super(2);
  }

  static final Pattern NUMBER = Pattern.compile("(\\d+)");

  static List<Long> longs(String data) {
    return NUMBER.matcher(data).results().map(MatchResult::group).map(Long::parseLong).toList();
  }

  boolean isInvalid(long l) {
    String s = String.valueOf(l);
    return s.length() % 2 == 0 && s.substring(0, s.length() / 2).equals(s.substring(s.length() / 2));
  }

  long invalidSum(List<Long> range, Function<Long, Boolean> isInvalid) {
    return LongStream.range(range.getFirst(), range.getLast() + 1)
        .filter(isInvalid::apply)
        .sum();
  }

  long allInvalid(Function<Long, Boolean> isInvalid) {
    return Support.partition(longs(data), 2)
        .mapToLong(range -> invalidSum(range, isInvalid))
        .sum();
  }

  @Override
  String part1() {
    return String.valueOf(allInvalid(this::isInvalid));
  }

  boolean isInvalid2(long l) {
    String s = String.valueOf(l);
    for (int i = 1; i <= s.length() / 2; i++) {
      if (s.length() % i == 0 && s.substring(0, i).repeat(s.length() / i).equals(s)) {
        return true;
      }
    }
    return false;
  }

  @Override
  String part2() {
    return String.valueOf(allInvalid(this::isInvalid2));
  }

  public static void main(String[] args) {
    Day day = new Day2() {
      @Override
      String getData() {
        return """
            11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
            1698522-1698528,446443-446449,38593856-38593862,565653-565659,
            824824821-824824827,2121212118-2121212124""";
      }
    };
    day.run("1227775554", "4174379265");
  }
}
