package advent2025;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

public abstract class Day {
  abstract String part1();

  abstract String part2();

  final int number;
  final List<String> input;
  final String data;

  protected Day(int number) {
    this.number = number;
    this.data = getData().trim();
    this.input = Support.splitInput(data);
  }

  static String compare(String expected, String actual) {
    if (expected == null) {
      return actual;
    }
    if (actual.equals(expected)) {
      return "PASS";
    }
    return "FAIL - expected (%s) != actual (%s)".formatted(expected, actual);
  }

  void run() {
    run(null, null);
  }

  record PartRun(String result, String duration) {
    static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("s.SSSSS");

    void print(int number, String expected) {
      System.out.printf("day %s part 1: (%s) %s%n", number, duration, compare(expected, result));
    }

    static PartRun run(Supplier<String> part) {
      Instant start = Instant.now();
      String result = part.get();
      Duration between = Duration.between(start, Instant.now());
      // https://stackoverflow.com/a/65586659
      return new PartRun(result, LocalTime.ofNanoOfDay(between.toNanos()).format(FORMAT));
    }
  }

  void run(String part1, String part2) {
    PartRun.run(this::part1).print(1, part1);
    PartRun.run(this::part2).print(2, part2);
  }

  private static final String INPUT_URL = "https://adventofcode.com/2025/day/%d/input";

  String getData() {
    try {
      return CachingHttpReader.getData(new URI(INPUT_URL.formatted(number)).toURL());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
