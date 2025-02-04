package defence.in.depth.unit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public final class TestData {
  public static Stream<Arguments> injectionStrings() throws URISyntaxException, IOException {
    // A selection from https://github.com/minimaxir/big-list-of-naughty-strings
    List<String> injectionStrings = Files.readAllLines(
        Path.of(TestData.class.getClassLoader().getResource("blns-injection.txt").toURI()));
    return injectionStrings.stream()
        .map(Arguments::of);

  }

  public static Stream<Arguments> strangeNames() throws URISyntaxException, IOException {
    // A selection from https://github.com/minimaxir/big-list-of-naughty-strings
    List<String> injectionStrings = Files.readAllLines(
        Path.of(TestData.class.getClassLoader().getResource("blns-names.txt").toURI()));
    return injectionStrings.stream()
        .map(Arguments::of);

  }


}
