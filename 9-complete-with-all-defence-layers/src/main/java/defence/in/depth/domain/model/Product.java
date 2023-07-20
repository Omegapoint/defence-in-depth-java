package defence.in.depth.domain.model;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.parameters.P;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductMarketId market;

    private final ProductDescription description;

    //TODO: add product description with validation in model

    public Product(ProductId id, ProductName name, ProductMarketId market, ProductDescription description) {
        this.id = id;
        this.name = name;
        this.market = market;
        this.description = description;
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
        return  description;
    }

    public Product addDescription(ProductDescription description) {
        return new Product(this.id, this.name, this.market, description);
    }
}
