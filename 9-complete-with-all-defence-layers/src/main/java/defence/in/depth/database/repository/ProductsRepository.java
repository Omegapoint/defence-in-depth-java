package defence.in.depth.database.repository;

import defence.in.depth.database.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class ProductsRepository {
    Map<String, ProductEntity> repo = Map.of("se1", new ProductEntity("se1", "ProductSweden", "SE"),
        "no1", new ProductEntity("no1", "ProductNorway", "NO"));

    public Optional<ProductEntity> findById(String productId) {
        // Please always use correct output encoding of input data "id" for
        // your query context. For example, parameterized SQL.
        // Here we have just hardcoded the id to the input.
        return Optional.ofNullable(repo.get(productId));
    }
}
