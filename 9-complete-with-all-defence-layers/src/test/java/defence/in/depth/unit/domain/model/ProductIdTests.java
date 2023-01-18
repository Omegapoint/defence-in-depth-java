package defence.in.depth.unit.domain.model;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import defence.in.depth.domain.model.ProductId;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("UnitTest")
public class ProductIdTests {

  @ParameterizedTest
  @MethodSource({"defence.in.depth.unit.TestData#injectionStrings", "invalidIds"})
  public void constructorShouldRejectInvalidData(String productId) {
    assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductId(productId));
  }

  @ParameterizedTest
  @NullAndEmptySource
  public void constructorShouldRejectEmptyData(String productId) {
    assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductId(productId));
  }

  @ParameterizedTest
  @ValueSource(strings = {"abcdefghi", "123456789"})
  public void constructorShouldAcceptValidData(String productId) {
    assertThat(new ProductId(productId).getProductId()).isEqualTo(productId);
  }


  public static Stream<Arguments> invalidIds() {
    return Stream.of(
        Arguments.of("no spaces"),
        Arguments.of("thisisanidthatistoolong"),
        Arguments.of("#")
    );
  }

}
