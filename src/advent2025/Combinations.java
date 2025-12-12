package advent2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Combinations {
  static <T> Stream<List<T>> combinations(List<T> data, int size) {
    if (size > data.size() || size < 0) {
      return Stream.empty();
    }

    if (size == 0) {
      return Stream.of(List.of());
    }

    // Calculate total combinations for better spliterator sizing
    long totalCombinations = binomialCoefficient(data.size(), size);

    return StreamSupport.stream(new Spliterators.AbstractSpliterator<>(
        totalCombinations,
        Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE) {

      final int[] indices = initIndices(size);
      boolean hasNext = true;

      // Use IntStream.range().toArray();
      private int[] initIndices(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
          arr[i] = i;
        }
        return arr;
      }

      @Override
      public boolean tryAdvance(Consumer<? super List<T>> action) {
        if (!hasNext) {
          return false;
        }

        // Build current combination
        List<T> combination = new ArrayList<>(size);
        for (int idx : indices) {
          combination.add(data.get(idx));
        }
        action.accept(combination);

        // Generate next combination using lexicographic ordering
        hasNext = nextCombination();
        return true;
      }

      private boolean nextCombination() {
        // Find the rightmost index that can be incremented
        int i = size - 1;
        while (i >= 0 && indices[i] == data.size() - size + i) {
          i--;
        }

        if (i < 0) {
          return false; // No more combinations
        }

        // Increment this index and reset all following indices
        indices[i]++;
        for (int j = i + 1; j < size; j++) {
          indices[j] = indices[j - 1] + 1;
        }

        return true;
      }
    }, false);
  }

  private static long binomialCoefficient(int n, int k) {
    if (k > n - k) {
      k = n - k;
    }
    long result = 1;
    for (int i = 0; i < k; i++) {
      result = result * (n - i) / (i + 1);
    }
    return result;
  }
}
