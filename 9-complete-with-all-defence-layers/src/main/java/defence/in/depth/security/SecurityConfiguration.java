package defence.in.depth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static defence.in.depth.domain.service.PermissionService.READ_PRODUCTS_SCOPE;
import static defence.in.depth.domain.service.PermissionService.WRITE_PRODUCTS_SCOPE;
import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;


// Note that you need to set prePostEnabled to true in order to get PreAuthorize to work
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // We will add antMatchers as a first layer of defence, but also make use of more granular checks using
        // @PreAuthorize on each controller.
        // The two last steps here, antMatchers("/**").denyALl() and .anyRequest().authenticated() will force
        // authorization and authentication on all endpoints by default.
        // antMatchers("/**").denyAll() will return 403 if not matched above, for example authenticated user without scopes
        // .anyRequest().authenticated() will force authentication on all endpoints.
        // This should always be last step of the chain to force opt-out security
        http
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers("/api/products/**").hasAnyAuthority(READ_PRODUCTS_SCOPE, WRITE_PRODUCTS_SCOPE)
                .antMatchers("/api/health/live").permitAll()
                .antMatchers("/api/health/**").authenticated()
                .antMatchers("/api/error/**").authenticated()
                .antMatchers("/**").denyAll() // Force authorization on all new endpoints
                .anyRequest().authenticated() // Force authentication on all new endpoints
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains("products.api"));
        OAuth2TokenValidator<Jwt> withIssuerAndAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withIssuerAndAudience);

        return jwtDecoder;
    }

}
