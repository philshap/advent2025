package advent2025;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

  private static Stream<String> days() {
    return IntStream.range(1, 12).mapToObj("advent2025.Day%s"::formatted);
  }

  private void runDays() {
    days()
        .map(name -> {
          try {
            return (Day) Class.forName(name).getDeclaredConstructors()[0].newInstance();
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .forEach(Day::run);
  }

  private void testDays() {
    days()
        .forEach(name -> {
          try {
            Class.forName(name).getMethod("main", String[].class).invoke(null, (Object) null);
          } catch (Exception e) {
            // ignore errors
          }
        });
  }

  public static void main(String[] args) {
    if (args.length == 1 && args[0].equals("test")) {
      new Main().testDays();
    } else {
      new Main().runDays();
    }
  }
}
