package defence.in.depth.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public final class ProductName {

    private final String productName;

    public ProductName(String productName){
        assertValidName(productName);
        this.productName = productName;
    }

    public static boolean isValidName(String id) {
        return StringUtils.isEmpty(id) || id.length() > 10 || !StringUtils.isAlphanumeric(id);
    }

    public static void assertValidName(String id) {
        if (!isValidName(id)) {
            throw new InvalidDomainPrimitiveException("Unable to parse product name");
        }
    }

    public String getName() {
        return productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductName that = (ProductName) o;
        return productName.equals(that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }
}
