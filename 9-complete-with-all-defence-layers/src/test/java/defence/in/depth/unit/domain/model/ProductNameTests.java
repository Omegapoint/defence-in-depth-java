package defence.in.depth.unit.domain.model;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import defence.in.depth.domain.model.ProductName;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("UnitTest")
public class ProductNameTests {

  @Test
  public void constructorShouldRejectInvalidData() {
    String productName = RandomStringUtils.randomAlphabetic(201);
    assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductName(productName));
  }

  @ParameterizedTest
  @NullAndEmptySource
  public void constructorShouldRejectEmptyData(String productName) {
    assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductName(productName));
  }

  @ParameterizedTest
  @MethodSource({"defence.in.depth.unit.TestData#strangeNames", "validNames"})
  public void constructorShouldAcceptValidData(String productName) {
    assertThat(new ProductName(productName).getName()).isEqualTo(productName);
  }


  public static Stream<Arguments> validNames() {
    return Stream.of(
        Arguments.of("My product name"),
        Arguments.of("Best product ever")
    );
  }

}
