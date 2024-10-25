package defence.in.depth.domain.mapper;

import defence.in.depth.database.entity.ProductEntity;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductDescription;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.model.ProductMarketId;
import defence.in.depth.domain.model.ProductName;
import defence.in.depth.dto.ProductDTO;

import java.util.Optional;

public final class ProductMapper {

    public static Product toProduct(ProductEntity productEntity) {
        ProductId productId = new ProductId(productEntity.id());
        ProductName productName = new ProductName(productEntity.name());
        ProductMarketId productMarketId = ProductMarketId.of(productEntity.market());
        ProductDescription productDescription = Optional.ofNullable(productEntity.description())
                .map(ProductDescription::new).orElse(null);
        return new Product.ProductBuilder(productId, productName, productMarketId).withDescription(productDescription).build();
    }

    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(product.getId().getProductId(),
            product.getName().getName(),
            product.getMarket().name(),
            Optional.ofNullable(product.getDescription()).map(ProductDescription::getDescription).orElse(null));
    }

    public static ProductEntity toEntity(Product product) {
        String productId = product.getId().getProductId();
        String productName = product.getName().getName();
        String productMarketId = product.getMarket().name();
        String productDescription = product.getDescription().getDescription();
        return new ProductEntity(productId, productName, productMarketId, productDescription);
    }
}