package defence.in.depth.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class ProductId {

    private final String productId;

    public ProductId(String productId) {
        assertValidId(productId);
        this.productId = productId;
    }

    public static boolean isValidId(String id) {
        return !StringUtils.isEmpty(id) && id.length() < 10 && StringUtils.isAlphanumeric(id);
    }

    public static void assertValidId(String id) {
        if (!isValidId(id)) {
            throw new InvalidDomainPrimitiveException("Unable to parse product id");
        }
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId1 = (ProductId) o;
        return productId.equals(productId1.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
