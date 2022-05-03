package defence.in.depth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
public final class ProductsController {

    private final Map<String, Product> products = Map.of(
        "se1", new Product("se1", "My Swedish Product", "se"),
        "no2", new Product("no2", "My Norwegian Product", "no"));

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") String id, Authentication authentication) {
        if (StringUtils.isEmpty(id) || id.length() > 10 || !StringUtils.isAlphanumeric(id)) {
            return ResponseEntity.badRequest().body("Parameter id is not well formed");
        }

        if (!hasAuthority(authentication, "urn:permissions:products:read")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Product product = products.get(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (!hasAuthority(authentication, "urn:permissions:market:" + product.market())) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(product);
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication
            .getAuthorities()
            .stream()
            .anyMatch(p -> p.getAuthority().equals(authority));
    }
}

record Product(String id, String name, String market) {
}

