package defence.in.depth.domain.model;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductMarketId market;

    public Product(ProductId id, ProductName name, ProductMarketId market) {
        this.id = id;
        this.name = name;
        this.market = market;
    }

    public Product(String id, String name, String market) {
        this.id = new ProductId(id);
        this.name = new ProductName(name);
        this.market = ProductMarketId.valueOf(market);
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
}
