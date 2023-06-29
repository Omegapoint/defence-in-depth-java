package defence.in.depth.unit.domain.service;

import defence.in.depth.database.entity.ProductEntity;
import defence.in.depth.database.repository.ProductsRepository;
import defence.in.depth.domain.exceptions.ProductMarketMismatchException;
import defence.in.depth.domain.exceptions.ProductNotFoundException;
import defence.in.depth.domain.exceptions.ReadProductNotAllowedException;
import defence.in.depth.domain.model.DomainEvent;
import defence.in.depth.domain.model.Product;
import defence.in.depth.domain.model.ProductId;
import defence.in.depth.domain.model.ProductMarketId;
import defence.in.depth.domain.service.AuditService;
import defence.in.depth.domain.service.PermissionService;
import defence.in.depth.domain.service.ProductsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.matches;

@Tag("UnitTest")
public class ProductServiceTests {

    private ProductsRepository productsRepository;
    private PermissionService permissionService;

    private AuditService auditService;

    @BeforeEach
    public void setupMocks() {
        this.productsRepository = Mockito.mock(ProductsRepository.class);
        this.permissionService = Mockito.mock(PermissionService.class);
        this.auditService = Mockito.mock(AuditService.class);
    }

    @Test
    void getById_throwsNotAllowed_IfNoValidReadScope() {

        Mockito.when(permissionService.canReadProducts()).thenReturn(false);

        ProductId productId = new ProductId("se1");
        ProductsService productsService = new ProductsService(productsRepository, permissionService, auditService);

        Assertions.assertThrows(ReadProductNotAllowedException.class, () -> productsService.getById(productId));

        Mockito.verify(permissionService, Mockito.times(1)).canReadProducts();
        Mockito.verify(auditService, Mockito.times(1)).log(DomainEvent.NO_ACCESS_TO_OPERATION, productId);
        Mockito.verifyNoMoreInteractions(productsRepository, permissionService, auditService);

    }

    @Test
    public void getById_throwsNotFound_IfValidClaimButNotExisting() {
        Mockito.when(permissionService.canReadProducts()).thenReturn(true);

        ProductsService productService = new ProductsService(productsRepository, permissionService, auditService);

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getById(new ProductId("notfound")));

        Mockito.verify(permissionService, Mockito.times(1)).canReadProducts();
        Mockito.verify(productsRepository, Mockito.times(1)).findById(matches("notfound"));
        Mockito.verifyNoMoreInteractions(productsRepository, permissionService, auditService);
    }

    @Test
    public void getById_throwsMarketMismatch_IfNotValidMarket() {
        ProductId productId = new ProductId("42");
        Mockito.when(productsRepository.findById(matches("42")))
                .thenReturn(Optional.of(new ProductEntity("42", "My name", "no")));
        Mockito.when(permissionService.canReadProducts()).thenReturn(true);
        Mockito.when(permissionService.hasPermissionToMarket(ProductMarketId.NO)).thenReturn(false);

        ProductsService productService = new ProductsService(productsRepository, permissionService, auditService);

        Assertions.assertThrows(ProductMarketMismatchException.class,
                () -> productService.getById(productId));

        Mockito.verify(permissionService, Mockito.times(1)).canReadProducts();
        Mockito.verify(permissionService, Mockito.times(1)).hasPermissionToMarket(ProductMarketId.NO);
        Mockito.verify(productsRepository, Mockito.times(1)).findById(matches("42"));
        Mockito.verify(auditService, Mockito.times(1)).log(DomainEvent.NO_ACCESS_TO_DATA, productId);
        Mockito.verifyNoMoreInteractions(productsRepository, permissionService, auditService);

    }

    // Testing successful resource access is important to verify that the
    // correct claim is needed to authorize access. If we did not, then
    // requiring a lower claim, e.g. "read:guest" would not be caught by the
    // NoValidReadClaim test above. This test will catch such configuration errors.
    @Test
    public void getById_ReturnsOk_IfValidClaims()
    {
        var productId = new ProductId("42");
        Mockito.when(productsRepository.findById(matches("42")))
                        .thenReturn(Optional.of(new ProductEntity("42", "My name", "se")));
        Mockito.when(permissionService.canReadProducts()).thenReturn(true);
        Mockito.when(permissionService.hasPermissionToMarket(ProductMarketId.SE)).thenReturn(true);

        ProductsService productsService = new ProductsService(productsRepository, permissionService, auditService);

        Product product = productsService.getById(productId);

        assertThat(product).isNotNull();

        Mockito.verify(permissionService, Mockito.times(1)).canReadProducts();
        Mockito.verify(permissionService, Mockito.times(1)).hasPermissionToMarket(ProductMarketId.SE);
        Mockito.verify(productsRepository, Mockito.times(1)).findById(matches("42"));
        Mockito.verify(auditService, Mockito.times(1)).log(DomainEvent.PRODUCT_READ, productId);
        Mockito.verifyNoMoreInteractions(productsRepository, permissionService, auditService);
    }

}
