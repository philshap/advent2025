package advent2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day10 extends Day {
  Day10() {
    super(10);
  }

  record Machine(int diagram, List<Integer> buttonMasks,
                 List<List<Integer>> buttons, List<Integer> jolts,
                 Map<List<Integer>, Integer> CACHE) {
    Machine(String line) {
      String[] split = line.split(" ");
      int diagram = 0;
      for (int i = 1; i < split[0].length() - 1; i++) {
        diagram |= (split[0].charAt(i) == '.' ? 0 : 1) << (i - 1);
      }
      var buttons = IntStream.range(1, split.length - 1)
          .mapToObj(i -> Support.integers(split[i]).stream().toList()).toList();
      // For each button, generate a mask of the bits changed by pressing that button.
      var buttonMasks =
          buttons.stream()
              .map(button ->
                  button.stream().reduce(0, (result, element) -> result | (1 << element))
              ).toList();
      var jolts = Support.integers(split[split.length - 1]);

      this(diagram, buttonMasks, buttons, jolts, new HashMap<>());
    }

    int pressButtons(int pressesMask) {
      int state = 0;
      for (int i = 0; i < buttonMasks.size(); i++) {
        if ((pressesMask & (1 << i)) != 0) {
          state ^= buttonMasks.get(i);
        }
      }
      return state;
    }

    // Given a target mask, find all button presses that generate it, returned as a bitmask
    // over button indexes
    int[] allPressesForMask(int mask) {
      // Try every possible combination of button presses
      return IntStream.range(0, 1 << buttonMasks.size())
          .filter(pressesMask -> pressButtons(pressesMask) == mask)
          .toArray();
    }

    List<Integer> newDiv2Target(List<Integer> prevTarget, int pressesMask) {
      List<Integer> target = new ArrayList<>(prevTarget);

      int[] counts = new int[target.size()];
      for (int j = 0; pressesMask != 0; pressesMask >>>= 1, j++) {
        if ((pressesMask & 1) != 0) {
          for (int i : buttons.get(j)) {
            counts[i]++;
          }
        }
      }

      for (int i = 0; i < target.size(); i++) {
        if (counts[i] > target.get(i)) {
          return List.of();
        }
        int rem = target.get(i) - counts[i];
        if ((rem & 1) == 1) {
          return List.of();
        }
        target.set(i, rem / 2);
      }

      return target;
    }

    Integer pressesForJoltageMemo(List<Integer> target) {
      return Support.computeIfAbsent(CACHE, target, this::pressesForJoltage);
    }

    Integer pressesForJoltage(List<Integer> target) {
      if (target.stream().allMatch(v -> v == 0)) {
        return 0;
      }

      int mask = 0;
      for (int i = 0; i < target.size(); i++) {
        if ((target.get(i) & 1) == 1) {
          mask |= (1 << i);
        }
      }

      var buttonPresses = allPressesForMask(mask);
      Integer best = null;
      for (int pressesMask : buttonPresses) {
        var newTarget = newDiv2Target(target, pressesMask);
        if (newTarget.isEmpty()) {
          continue;
        }
        Integer sub = pressesForJoltageMemo(newTarget);
        if (sub != null) {
          int cost = Integer.bitCount(pressesMask) + 2 * sub;
          best = (best == null) ? cost : Math.min(best, cost);
        }
      }

      return best;
    }

    int pressesForLights() {
      return Arrays.stream(allPressesForMask(diagram)).map(Integer::bitCount).min().orElseThrow();
    }

    int pressesForJoltage() {
      return pressesForJoltage(jolts);
    }
  }

  @Override
  String part1() {
    int presses = input.stream().map(Machine::new).mapToInt(Machine::pressesForLights).sum();
    return String.valueOf(presses);
  }

  @Override
  String part2() {
    int presses = input.stream().map(Machine::new).mapToInt(Machine::pressesForJoltage).sum();
    return String.valueOf(presses);
  }

  static void main() {
    Day day = new Day10() {
      @Override
      String getData() {
        return """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""";
      }
    };
    day.run("7", "33");
    day = new Day10();
    day.run("530", "20172");
  }
}
