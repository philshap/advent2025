package advent2025;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

record Pos(int x, int y) {
  static final Pos U = new Pos(0, -1);
  static final Pos R = new Pos(1, 0);
  static final Pos D = new Pos(0, 1);
  static final Pos L = new Pos(-1, 0);

  static final Collection<Pos> EDGES = List.of(U, R, D, L,
      new Pos(-1, -1), new Pos(-1, 1), new Pos(1, -1), new Pos(1, 1));

  static Stream<Pair<Pos, Character>> streamOf(List<String> input) {
    return IntStream.range(0, input.getFirst().length())
        .boxed()
        .flatMap(x -> IntStream.range(0, input.size())
            .mapToObj(y -> Pair.of(new Pos(x, y), input.get(y).charAt(x))));
  }

  Pos(List<Integer> pair) {
    this(pair.getFirst(), pair.getLast());
  }

  Pos plus(Pos p2) {
    return new Pos(x + p2.x, y + p2.y);
  }
  Pos minus(Pos p2) {
    return new Pos(x - p2.x, y - p2.y);
  }
}
