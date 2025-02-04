package defence.in.depth.unit.domain.service;

import defence.in.depth.domain.model.ProductMarketId;
import defence.in.depth.domain.service.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class PermissionServiceTest {
    private Authentication authentication;

    @BeforeEach
    public void setupMocks() {
        authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return authentication;
            }

            @Override
            public void setAuthentication(Authentication authentication) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    void hasPermissionsToSE() {
        PermissionService permissionService = new PermissionService();

        Assertions.assertTrue(permissionService.hasPermissionToMarket(ProductMarketId.SE));
    }

    @Test
    void hasNoPermissionsToNO() {
        PermissionService permissionService = new PermissionService();

        Assertions.assertFalse(permissionService.hasPermissionToMarket(ProductMarketId.NO));
    }

    @Test
    void hasNoPermissionsToFI() {
        PermissionService permissionService = new PermissionService();

        Assertions.assertFalse(permissionService.hasPermissionToMarket(ProductMarketId.FI));
    }

    @Test
    void hasOnlyWrite_DeniesRead() {
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocationOnMock -> List.of(
                new SimpleGrantedAuthority(PermissionService.WRITE_PRODUCTS_SCOPE)
        ));

        PermissionService permissionService = new PermissionService();
        boolean read = permissionService.canReadProducts();
        boolean write = permissionService.canWriteProducts();

        Assertions.assertTrue(write);
        Assertions.assertFalse(read);
    }

    @Test
    void hasOnlyRead_DeniesWrite() {
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocationOnMock -> List.of(
                new SimpleGrantedAuthority(PermissionService.READ_PRODUCTS_SCOPE)
        ));

        PermissionService permissionService = new PermissionService();
        boolean read = permissionService.canReadProducts();
        boolean write = permissionService.canWriteProducts();

        Assertions.assertFalse(write);
        Assertions.assertTrue(read);
    }
}
