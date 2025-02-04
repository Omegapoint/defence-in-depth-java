package defence.in.depth.unit.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import defence.in.depth.domain.model.ProductMarketId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
public class ProductMarketIdTests {

    @ParameterizedTest
    @MethodSource({"defence.in.depth.unit.TestData#injectionStrings"})
    public void constructorShouldRejectInvalidData(String market) {
        assertThrows(InvalidDomainPrimitiveException.class, () -> ProductMarketId.of(market));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void constructorShouldRejectEmptyData(String market) {
        assertThrows(InvalidDomainPrimitiveException.class, () -> ProductMarketId.of(market));
    }

    @ParameterizedTest
    @ValueSource(strings = {"se", "no", "fi", "FI"})
    public void constructorShouldAcceptValidData(String market) {
        assertThat(ProductMarketId.of(market).name()).isEqualToIgnoringCase(market);
    }

}
