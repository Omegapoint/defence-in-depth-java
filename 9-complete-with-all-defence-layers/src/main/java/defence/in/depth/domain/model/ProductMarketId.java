package defence.in.depth.domain.model;

import defence.in.depth.domain.exceptions.InvalidDomainPrimitiveException;

import java.util.Arrays;

public enum ProductMarketId {
    SE,
    NO,
    FI;

    public static boolean isValidId(String id) {
        return Arrays.stream(values())
                .anyMatch(marketId -> marketId.name().equalsIgnoreCase(id));
    }

    public static void assertValidId(String id) {
        if (!isValidId(id)) {
            throw new InvalidDomainPrimitiveException("Unable to parse product id");
        }
    }

    public static ProductMarketId of(String market) {
        assertValidId(market);
        for (ProductMarketId marketId : ProductMarketId.values()) {
            if (marketId.name().equalsIgnoreCase(market)) {
                return marketId;
            }
        }
        throw new InvalidDomainPrimitiveException("Unable to parse product id");
    }

}
