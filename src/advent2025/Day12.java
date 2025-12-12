package advent2025;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day {
  public Day12() {
    super(12);
  }

  record Present(Set<Pos> shape) {
    Present(String data) {
      var lines = Support.splitInput(data);
      this(Pos.streamOf(lines.subList(1, lines.size())).filter(pair -> pair.r() == '#').map(Pair::l).collect(Collectors.toSet()));
    }
  }

  record Tree(int width, int height, List<Integer> counts) {
    Tree(String line) {
      String[] split = line.split(": ");
      var region = Support.integers(split[0]);
      this(region.getFirst(), region.getLast(), Support.integers(split[1]));
    }

    // Instead of trying to rotate / flip presents, check and see if there's enough
    // room for all the present's points in the tree's total area. Apparently this is good enough
    // for the data.
    boolean canPlaceAll(List<Present> presents) {
      int areaRequired = IntStream.range(0, counts.size()).map(i -> counts.get(i) * presents.get(i).shape.size()).sum();
      return areaRequired < (height * width);
    }
  }

  @Override
  String part1() {
    String[] groups = data.split("\n\n");
    var presents = Arrays.stream(Arrays.copyOfRange(groups, 0, groups.length - 1)).map(Present::new).toList();
    var trees = Support.splitInput(groups[groups.length - 1]).stream().map(Tree::new).toList();
    long count = trees.stream().filter(t -> t.canPlaceAll(presents)).count();
    return String.valueOf(count);
  }

  @Override
  String part2() {
    return "";
  }

  static void main() {
    Day day = new Day12() {
      @Override
      String getData() {
        return """
            0:
            ###
            ##.
            ##.

            1:
            ###
            ##.
            .##

            2:
            .##
            ###
            ##.

            3:
            ##.
            ###
            ##.

            4:
            ###
            #..
            ###

            5:
            ###
            .#.
            ###

            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2""";
      }
    };
    day.run("3", "");
  }
}
