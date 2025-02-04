package defence.in.depth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public final class ProductsController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id) || id.length() > 10 || !StringUtils.isAlphanumeric(id)) {
            return ResponseEntity.badRequest().body("Parameter id is not well formed");
        }
        return ResponseEntity.ok("product");
    }

}

