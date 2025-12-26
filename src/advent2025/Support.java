package advent2025;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Support {
  Pattern NUMBER = Pattern.compile("(-?\\d+)");

  static <T> Stream<List<T>> partition(List<T> source, int length) {
    return partition(source, length, 0);
  }

  static <T> Stream<List<T>> partition(List<T> source, int length, int overlap) {
    int step = length - overlap;
    return IntStream.range(0, (source.size() - overlap + step - 1) / step)
        .mapToObj(n -> source.subList(n * step, n * step + length));
  }

  static List<Integer> integers(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Integer::parseInt).toList();
  }

  static Stream<Long> longs(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Long::parseLong);
  }

  static List<String> splitInput(String input) {
    return Arrays.asList(input.split("\n"));
  }

  ///  A computeIfAbsent that supports recursive functions.
  static <T, R> R computeIfAbsent(Map<T, R> cache, T input, Function<T, R> func) {
    var result = cache.get(input);
    if (result != null) {
      return result;
    }
    result = func.apply(input);
    cache.put(input, result);
    return result;
  }
}
