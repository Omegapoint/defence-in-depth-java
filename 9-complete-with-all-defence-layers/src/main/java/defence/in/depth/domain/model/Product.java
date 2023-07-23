package defence.in.depth.domain.model;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.parameters.P;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductMarketId market;
    private ProductDescription description;

    private Product(ProductId id, ProductName name, ProductMarketId market) {
        this.id = id;
        this.name = name;
        this.market = market;
        this.description = new ProductDescription(null);
    }

    public Product(String id, String name, String market, String description) {
        this.id = new ProductId(id);
        this.name = new ProductName(name);
        this.market = ProductMarketId.of(market);
        this.description = new ProductDescription(description);
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

    public static class ProductBuilder {
        private Product product;

        public ProductBuilder(ProductId id, ProductName name, ProductMarketId market) {
            this.product = new Product(id, name, market);
        }

        public ProductBuilder(Product product) {
            this.product = new Product(product.id, product.name, product.market);
        }

        public ProductBuilder withDescription(ProductDescription description) {
            this.product.description = description;
            return this;
        }

        public Product build() {
            Product result = product;
            this.product = null;
            return result;
        }
    }
}
