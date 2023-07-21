package defence.in.depth.domain.mapper;

import defence.in.depth.database.entity.ProductEntity;
import defence.in.depth.domain.model.*;
import defence.in.depth.dto.ProductDTO;

public final class ProductMapper {

    public static Product toProduct(ProductEntity productEntity) {
        ProductId productId = new ProductId(productEntity.id());
        ProductName productName = new ProductName(productEntity.name());
        ProductMarketId productMarketId = ProductMarketId.of(productEntity.market());
        ProductDescription productDescription = new ProductDescription(productEntity.description());
        return new Product(productId, productName, productMarketId, productDescription);
    }

    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(product.getId().getProductId(),
            product.getName().getName(),
            product.getMarket().name(),
            product.getDescription().getProductDescription());
    }

    public static ProductEntity toEntity(Product product) {
        String productId = product.getId().getProductId();
        String productName = product.getName().getName();
        String productMarketId = product.getMarket().name();
        String productDescription = product.getDescription().getProductDescription();
        return new ProductEntity(productId, productName, productMarketId, productDescription);
    }
}