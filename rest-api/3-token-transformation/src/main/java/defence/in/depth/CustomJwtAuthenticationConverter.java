package defence.in.depth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        addPermissionIfScope(authorities, source, "products.read", new SimpleGrantedAuthority("urn:permissions:products:read"));
        addPermissionIfScope(authorities, source, "products.write", new SimpleGrantedAuthority("urn:permissions:products:write"));

        // This is typically a service call or database query, based on incoming identity
        authorities.add(new SimpleGrantedAuthority("urn:permissions:market:se"));
        String principalClaimValue = source.getClaimAsString("sub");
        return new JwtAuthenticationToken(source, authorities, principalClaimValue);
    }

    private void addPermissionIfScope(Collection<GrantedAuthority> authorities, Jwt jwt, String scope, SimpleGrantedAuthority simpleGrantedAuthority) {
        String allScopes = jwt.getClaimAsString("scope");
        if (allScopes != null && allScopes.contains(scope))
            authorities.add(simpleGrantedAuthority);
    }
}
