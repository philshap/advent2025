package advent2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day11 extends Day {
  Day11() {
    super(11);
  }

  record Graph(Map<String, List<String>> nodes) {
    Graph(List<String> input) {
      this(input.stream().map(line -> line.split(":? "))
          .collect(Collectors.toMap(
              split -> split[0],
              split -> Arrays.asList(Arrays.copyOfRange(split, 1, split.length)))));
    }

    int countAllPaths(String start) {
      Queue<String> queue = new LinkedList<>();
      queue.add(start);
      int count = 0;
      while (!queue.isEmpty()) {
        String head = queue.remove();
        if (head.equals("out")) {
          count++;
        } else {
          queue.addAll(nodes.get(head));
        }
      }
      return count;
    }

    record State(String start, boolean seenDac, boolean seenFft) {
      boolean atEnd() {
        return start.equals("out");
      }

      int count() {
        return seenDac && seenFft ? 1 : 0;
      }

      State next(String next) {
        return new State(next, seenDac || next.equals("dac"), seenFft || next.equals("fft"));
      }
    }

    static final Map<State, Long> CACHE = new HashMap<>();

    long memoCountPaths(State state) {
      return Support.computeIfAbsent(CACHE, state, this::countPaths);
    }

    long countPaths(State state) {
      if (state.atEnd()) {
        return state.count();
      }
      return nodes.getOrDefault(state.start, List.of()).stream().map(state::next).mapToLong(this::memoCountPaths).sum();
    }
  }

  @Override
  String part1() {
    Graph graph = new Graph(input);
    var count = graph.countAllPaths("you");
    return String.valueOf(count);
  }

  @Override
  String part2() {
    Graph graph = new Graph(input);
    var count = graph.countPaths(new Graph.State("svr", false, false));
    return String.valueOf(count);
  }

  static void main() {
    Day day = new Day11() {
      @Override
      String getData() {
        return """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            svr: aaaa bbbb
            aaaa: fft
            fft: cccc
            bbbb: tty
            tty: cccc
            cccc: dddd eeee
            dddd: hub
            hub: ffff
            eeee: dac
            dac: ffff
            ffff: gggg hhhh
            gggg: out
            hhhh: out""";
      }
    };
    day.run("5", "2");
  }
}
