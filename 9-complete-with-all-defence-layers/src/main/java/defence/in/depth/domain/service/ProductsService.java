package defence.in.depth.domain.service;

import defence.in.depth.database.entity.ProductEntity;
import defence.in.depth.database.repository.ProductsRepository;
import defence.in.depth.domain.exceptions.ProductMarketMismatchException;
import defence.in.depth.domain.exceptions.ProductNotFoundException;
import defence.in.depth.domain.exceptions.ReadProductNotAllowedException;
import defence.in.depth.domain.exceptions.WriteProductNotAllowedException;
import defence.in.depth.domain.mapper.ProductMapper;
import defence.in.depth.domain.model.DomainEvent;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductDescription;
import defence.in.depth.domain.model.ProductId;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final PermissionService permissionService;

    private final AuditService auditService;

    public ProductsService(ProductsRepository productsRepository, PermissionService permissionService, AuditService auditService) {
        this.productsRepository = productsRepository;
        this.permissionService = permissionService;
        this.auditService = auditService;
    }

    public Product getById(ProductId productId) {
        if (!permissionService.canReadProducts()) {
            auditService.log(DomainEvent.NO_ACCESS_TO_OPERATION, productId);
            throw new ReadProductNotAllowedException("User not allowed to read product");
        }

        Product product = productsRepository
            .findById(productId.getProductId())
            .map(ProductMapper::toProduct)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (!permissionService.hasPermissionToMarket(product.getMarket())) {
            auditService.log(DomainEvent.NO_ACCESS_TO_DATA, productId);
            throw new ProductMarketMismatchException("User market does not match product market");
        }

        auditService.log(DomainEvent.PRODUCT_READ, productId);
        return product;
    }

    //TODO: Add method for product description with input description and product id. Also input validation and access control
    public void addDescription(ProductId productId, ProductDescription productDescription) {
        if (!permissionService.canWriteProducts()) {
            auditService.log(DomainEvent.NO_ACCESS_TO_OPERATION, productId);
            throw new WriteProductNotAllowedException("User not allowed to write to product");
        }

        Product product = productsRepository
                .findById(productId.getProductId())
                .map(ProductMapper::toProduct)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (!permissionService.hasPermissionToMarket(product.getMarket())) {
            auditService.log(DomainEvent.NO_ACCESS_TO_DATA, productId);
            throw new ProductMarketMismatchException("User market does not match product market");
        }

        Product productWithDescription = product.addDescription(productDescription);
        ProductEntity productEntity = ProductMapper.toEntity(productWithDescription);
        productsRepository.save(productId.getProductId(), productEntity);
        auditService.log(DomainEvent.PRODUCT_WRITE, productId);
    }
}
