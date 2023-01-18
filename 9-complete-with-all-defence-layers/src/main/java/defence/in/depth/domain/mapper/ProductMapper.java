package defence.in.depth.domain.mapper;

import defence.in.depth.database.entity.ProductEntity;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.model.ProductMarketId;
import defence.in.depth.domain.model.ProductName;
import defence.in.depth.dto.ProductDTO;

public final class ProductMapper {

    public static Product toProduct(ProductEntity productEntity) {
        ProductId productId = new ProductId(productEntity.id());
        ProductName productName = new ProductName(productEntity.name());
        ProductMarketId productMarketId = ProductMarketId.of(productEntity.market());
        return new Product(productId, productName, productMarketId);
    }

    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(product.getId().getProductId(),
            product.getName().getName(),
            product.getMarket().name());
    }

}