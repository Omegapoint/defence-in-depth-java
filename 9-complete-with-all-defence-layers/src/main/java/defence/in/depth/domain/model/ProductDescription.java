package defence.in.depth.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;

public class ProductDescription {

    private final String productDescription;

    @JsonCreator
    public ProductDescription(@JsonProperty("productDescription") String productDescription) {
        assertValidDescription(productDescription);
        this.productDescription = productDescription;
    }

    public String getDescription() {
        return this.productDescription;
    }

    public static boolean isValidDescription(String description) {
        if (description == null) {
            return false;
        }
        if (description.length() < 10 || description.length() > 300) {
            return false;
        }
        return true;
    }

    public static void assertValidDescription(String description) {
        if (!isValidDescription(description)) {
            throw new InvalidDomainPrimitiveException("Unable to parse product description");
        }
    }
}
