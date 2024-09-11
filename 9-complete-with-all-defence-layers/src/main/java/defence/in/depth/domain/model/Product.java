package defence.in.depth.domain.model;

import static org.apache.commons.lang3.Validate.validState;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductMarketId market;
    private ProductDescription description;

    private Product(ProductId id, ProductName name, ProductMarketId market) {
        this.id = id;
        this.name = name;
        this.market = market;
        this.description = null;
    }

    public ProductId getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public ProductMarketId getMarket() {
        return market;
    }

    public ProductDescription getDescription() {
        return description;
    }

    private void checkInvariants() throws IllegalStateException {
        validState(id.getProductId().toLowerCase().startsWith(market.name().toLowerCase()));
    }
    //TODO: add check invariants assert that id starts with market id
    public static class ProductBuilder {
        private Product product;

        public ProductBuilder(ProductId id, ProductName name, ProductMarketId market) {
            this.product = new Product(id, name, market);
        }

        public ProductBuilder(Product product) {
            this.product = new Product(product.id, product.name, product.market);
        }

        public ProductBuilder withDescription(ProductDescription description) {
            validState(product != null);
            this.product.description = description;
            return this;
        }

        public Product build() {
            validState(product != null);
            product.checkInvariants();
            Product result = product;
            this.product = null;
            return result;
        }
    }
}
