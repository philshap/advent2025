package advent2025;

import java.util.ArrayList;
import java.util.List;

public class Day5 extends Day {
  public Day5() {
    super(5);
  }

  record Range(long low, long high) {
    Range(String line) {
      String[] split = line.split("-");
      this(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }
    boolean contains(long id) {
      return low <= id && id <= high;
    }
    Range merge(Range other) {
      // disjoint
      if ((high + 1) < other.low || (other.high + 1) < low) {
        return null;
      }
      // fully contained
      if (low >= other.low && high <= other.high) {
        return other;
      }
      if (other.low >= low && other.high <= high) {
        return this;
      }
      // merge
      return new Range(Math.min(low, other.low), Math.max(high, other.high));
    }
  }

  @Override
  String part1() {
    String[] split = data.split("\n\n");
    List<Range> ranges = Support.splitInput(split[0]).stream().map(Range::new).toList();
    long count = Support.longs(split[1]).filter(id -> ranges.stream().anyMatch(range -> range.contains(id))).count();
    return String.valueOf(count);
  }

  record Merge(List<Range> merged, Range merge) {}

  Merge findMerge(List<Range> ranges) {
    for (int i = 0; i < ranges.size(); i++) {
      for (int j = i + 1; j < ranges.size(); j++) {
        var merged = ranges.get(i).merge(ranges.get(j));
        if (merged != null) {
          return new Merge(List.of(ranges.get(i), ranges.get(j)), merged);
        }
      }
    }
    return null;
  }

  List<Range> mergeRanges(List<Range> ranges) {
    ranges = new ArrayList<>(ranges);
    Merge merge;
    while ((merge = findMerge(ranges)) != null) {
      ranges.removeAll(merge.merged);
      ranges.add(merge.merge);
    }
    return ranges;
  }

  @Override
  String part2() {
    String[] split = data.split("\n\n");
    List<Range> ranges = Support.splitInput(split[0]).stream().map(Range::new).toList();
    long total = mergeRanges(ranges).stream().mapToLong(range -> (range.high - range.low) + 1).sum();
    return String.valueOf(total);
  }

  static void main() {
    Day day = new Day5() {
      @Override
      String getData() {
        return """
            3-5
            10-14
            16-20
            12-18

            1
            5
            8
            11
            17
            32""";
      }
    };
    day.run("3", "14");
  }
}
