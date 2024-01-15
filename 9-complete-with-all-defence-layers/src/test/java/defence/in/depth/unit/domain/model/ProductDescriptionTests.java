package defence.in.depth.unit.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import defence.in.depth.domain.model.ProductDescription;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

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
    @NullSource
    @MethodSource("validDescriptions")
    public void constructorShouldAcceptValidData(String description) {
        assertThat(new ProductDescription(description).getDescription()).isEqualTo(description);
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
                Arguments.of(RandomStringUtils.randomAlphabetic(301))
        );
    }

}