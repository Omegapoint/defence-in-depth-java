package defence.in.depth.unit.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import defence.in.depth.config.RestResponseEntityExceptionHandler;
import defence.in.depth.controller.ProductsController;
import defence.in.depth.domain.exceptions.ProductNotFoundException;
import defence.in.depth.domain.exceptions.ReadProductNotAllowedException;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.model.ProductMarketId;
import defence.in.depth.domain.model.ProductName;
import defence.in.depth.domain.service.ProductsService;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@Tag("UnitTest")
@SecurityTestExecutionListeners
@WebMvcTest(value = ProductsController.class)
public class ProductsControllerTests {
  @Autowired
  private MockMvc mvc;
  private ProductsService productsService;

  @BeforeEach
  public void setupMocks() {
    this.productsService = Mockito.mock(ProductsService.class);
    this.mvc = MockMvcBuilders
        .standaloneSetup(new ProductsController(productsService))
        .setControllerAdvice(new RestResponseEntityExceptionHandler())
        .build();
  }

  @Test
  public void getProductsByIdShouldReturn403WhenCanNotRead() throws Exception {
    when(productsService.getById(any()))
        .thenThrow(new ReadProductNotAllowedException("User not allowed to read product"));

    this.mvc.perform(get("/api/products/se1"))
        .andExpect(status().isForbidden());

  }

  @Test
  public void getProductsByIdShouldReturn200WhenAuthorized() throws Exception {
    when(productsService.getById(any())).thenReturn(
        new Product(
            new ProductId("se1"),
            new ProductName("ProductSweden"),
            ProductMarketId.SE
        )
    );

    this.mvc.perform(get("/api/products/se1"))
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @MethodSource({"idInjections", "invalidIds"})
  public void getProductsByIdShouldReturn400WhenInvalidId(String productId) throws Exception {
    when(productsService.getById(any())).thenReturn(
        new Product(
            new ProductId("se1"),
            new ProductName("ProductSweden"),
            ProductMarketId.SE
        )
    );

    this.mvc.perform(get("/api/products/{id}", productId))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getProductsByIdShouldReturn404WhenNotFound() throws Exception {
    when(productsService.getById(any()))
        .thenThrow(new ProductNotFoundException("Product not found"));

    this.mvc.perform(get("/api/products/def")) // This is a valid, non-existing id
        .andExpect(status().isNotFound());
  }

  public static Stream<Arguments> idInjections() {
    return Stream.of(
        Arguments.of("<script>"),
        Arguments.of("'1==1"),
        Arguments.of("--sql")
    );
  }

  public static Stream<Arguments> invalidIds() {
    return Stream.of(
        Arguments.of("no spaces"),
        Arguments.of("thisisanidthatistoolong"),
        Arguments.of("#")
    );
  }


}
