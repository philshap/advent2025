package advent2025;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day8 extends Day {
  public Day8() {
    super(8);
  }

  int connectionLimit = 1000;

  record Box(int x, int y, int z) {
     Box(List<Integer> ints) {
       this(ints.get(0), ints.get(1), ints.get(2));
     }
  }

  double distance(List<Box> pair) {
    Box b1 = pair.getFirst();
    Box b2 = pair.getLast();
    return Math.sqrt(Math.pow(b1.x - b2.x, 2) + Math.pow(b1.y - b2.y, 2) + Math.pow(b1.z - b2.z, 2));
  }

  // TODO: make circuits immutable
  void connect(List<Box> pair, List<Set<Box>> circuits) {
    Set<Box> c1 = null;
    Set<Box> c2 = null;
    for (var circuit : circuits) {
      if (circuit.contains(pair.getFirst())) {
        c1 = circuit;
      }
      if (circuit.contains(pair.getLast())) {
        c2 = circuit;
      }
      if (c1 != null && c2 != null) {
        if (!c1.equals(c2)) {
          c1.addAll(c2);
          circuits.remove(c2);
        }
        return;
      }
    }
  }

  List<Box> createBoxes() {
    return Support.partition(Support.integers(data), 3).map(Box::new).toList();
  }

  List<Set<Box>> createCircuits(List<Box> boxes) {
    return boxes.stream().map(Set::of).map(HashSet::new).collect(Collectors.toList());
  }

  Stream<List<Box>> boxPairsByDistance(List<Box> boxes) {
    return Combinations.combinations(boxes, 2)
        .sorted(Comparator.comparingDouble(this::distance));
  }

  @Override
  String part1() {
    List<Box> boxes = createBoxes();
    List<Set<Box>> circuits = createCircuits(boxes);
    boxPairsByDistance(boxes)
        .limit(connectionLimit)
        .forEach(pair -> connect(pair, circuits));

    int product = circuits.stream().map(Set::size).sorted(Comparator.reverseOrder()).limit(3).reduce(1, (i, j) -> i * j);
    return String.valueOf(product);
  }

  @Override
  String part2() {
    List<Box> boxes = createBoxes();
    List<Set<Box>> circuits = createCircuits(boxes);

    long lastConnect =
        boxPairsByDistance(boxes)
            .flatMapToLong(pair -> {
              connect(pair, circuits);
              if (circuits.size() == 1) {
                return LongStream.of((long) pair.getFirst().x() * pair.getLast().x());
              }
              return LongStream.of();
            }).findFirst().orElseThrow();

    return String.valueOf(lastConnect);
  }

  static void main() {
    Day8 day = new Day8() {
      @Override
      String getData() {
        return """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689""";
      }
    };
    day.connectionLimit = 10;
    day.run("40", "25272");
  }
}
