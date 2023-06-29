package defence.in.depth.domain.service;

import defence.in.depth.domain.model.ProductMarketId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public static final String READ_PRODUCTS_SCOPE = "SCOPE_products.read";
    public static final String WRITE_PRODUCTS_SCOPE = "SCOPE_products.write";

    public boolean canReadProducts() {
        return ifScope(READ_PRODUCTS_SCOPE);
    }

    public boolean canWriteProducts() {
        return ifScope(WRITE_PRODUCTS_SCOPE);
    }

    public boolean hasPermissionToMarket(ProductMarketId productMarketId) {
        return getMarketForAuthenticatedUser().equals(productMarketId);
    }

    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private ProductMarketId getMarketForAuthenticatedUser() {
        // This is typically a service call or database query, based on identity
        return ProductMarketId.SE;
    }

    private boolean ifScope(String scope) {
        return SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals(scope));
    }

}
