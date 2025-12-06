package advent2025;

import java.util.ArrayList;
import java.util.List;

public class Day6 extends Day {
  public Day6() {
    super(6);
  }

  @Override
  String part1() {
    int lines = input.size();
    String[] ops = input.getLast().split("\\s+");
    List<Integer> allNumbers = Support.integers(data);
    int width = allNumbers.size() / (lines - 1);
    long sum = 0;
    for (int i = 0; i < width; i++) {
      long column;
      if (ops[i].equals("+")) {
        column = 0;
        for (int j = i; j < allNumbers.size(); j += width) {
          column += allNumbers.get(j);
        }
      } else {
        column = 1;
        for (int j = i; j < allNumbers.size(); j += width) {
          column *= allNumbers.get(j);
        }
      }
      sum += column;
    }
    return String.valueOf(sum);
  }

  @Override
  String part2() {
    int lines = input.size();
    int opRow = lines - 1;
    int width = input.getFirst().length();
    long sum = 0;
    int column = width - 1;
    int row = 0;
    StringBuilder digit = new StringBuilder();
    List<Integer> numbers = new ArrayList<>();
    while (column >= 0) {
      char ch = data.charAt(column + row * (width + 1));
      if (row == opRow) {
        numbers.add(Integer.parseInt(digit.toString().trim()));
        digit.delete(0, digit.length());
        if (ch == '+') {
          sum += numbers.stream().mapToLong(Integer::longValue).sum();
          numbers.clear();
          column--;
        } else if (ch == '*') {
          sum += numbers.stream().mapToLong(Integer::longValue).reduce(1, (l1, l2) -> l1 * l2);
          numbers.clear();
          column--;
        }
        row = 0;
        column--;
      } else {
        digit.append(ch);
        row++;
      }
    }
    return String.valueOf(sum);
  }

  static void main() {
    Day day = new Day6() {
      @Override
      String getData() {
        return """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s""";
      }
    };
    day.run("4277556", "3263827");
  }
}
