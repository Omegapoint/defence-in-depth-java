package defence.in.depth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public final class ProductsController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id, Authentication authentication) {
        if (StringUtils.isEmpty(id) || id.length() > 10 || !StringUtils.isAlphanumeric(id)) {
            return ResponseEntity.badRequest().body("Parameter id is not well formed");
        }

        if (!hasAuthority(authentication, "urn:permissions:products:read")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok("product");
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication
            .getAuthorities()
            .stream()
            .anyMatch(p -> p.getAuthority().equals(authority));
    }

}

