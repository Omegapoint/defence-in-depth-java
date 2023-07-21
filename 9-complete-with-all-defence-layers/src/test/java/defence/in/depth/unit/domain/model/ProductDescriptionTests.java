package defence.in.depth.unit.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import defence.in.depth.domain.model.ProductDescription;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.model.ProductName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
public class ProductDescriptionTests {

    @ParameterizedTest
    @MethodSource("invalidDescriptions")
    public void constructorShouldRejectInvalidData(String description) {
        assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductDescription(description));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void constructorShouldRejectEmptyData(String description) {
        assertThrows(InvalidDomainPrimitiveException.class, () -> new ProductDescription(description));
    }

    @ParameterizedTest
    //@MethodSource({"defence.in.depth.unit.TestData#strangeNames", "validNames"})
    @MethodSource("validDescriptions")
    public void constructorShouldAcceptValidData(String description) {
        assertThat(new ProductDescription(description).getProductDescription()).isEqualTo(description);
    }

    public static Stream<Arguments> validDescriptions() {
        return Stream.of(
                Arguments.of("This is a good description"),
                Arguments.of("Very good! 10% off. *may vary")
        );
    }

    public static Stream<Arguments> invalidDescriptions() {
        return Stream.of(
                Arguments.of("tooshort"),
                Arguments.of("<script>forbidden char</script>"),
                Arguments.of(RandomStringUtils.randomAlphabetic(301))
        );
    }

}