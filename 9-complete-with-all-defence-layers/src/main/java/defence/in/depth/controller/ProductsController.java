package defence.in.depth.controller;

import defence.in.depth.domain.mapper.ProductMapper;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.service.ProductsService;
import defence.in.depth.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static defence.in.depth.domain.service.PermissionService.READ_PRODUCTS_SCOPE;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    // More granular control using preauthorize as a compliment to antMatchers defined in the security configuration.
    // Note that the main permission check is still in the core business domain in the productsService
    @PreAuthorize("hasAuthority('" + READ_PRODUCTS_SCOPE + "')")
    @GetMapping("/{id}")
    // The type of id is a domain primitive ProductId and if the validation fails in the constructor
    // Spring will return 400 Bad Request for us when we use it as a `PathVariable`.
    public ResponseEntity<ProductDTO> getById(@PathVariable ProductId id) {
        Product product = productsService.getById(id);
        ProductDTO productDto = ProductMapper.toProductDTO(product);
        return ResponseEntity.ok(productDto);
    }

}
