package br.app.cashew.feature01.authentication.config;

import br.app.cashew.feature01.authentication.service.authentication.CustomUserDetailsService;
import br.app.cashew.feature01.authentication.service.jwt.JwtAccessTokenJtiValidator;
import br.app.cashew.feature01.authentication.service.jwt.JwtAlgorithmValidator;
import br.app.cashew.feature01.authentication.util.key.RSAKeyProperties;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.List;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.AUD;

@Configuration
@EnableAsync
public class SecurityConfig {

    private static final String ROLE_OWNER = "ROLE_OWNER";
    private static final String ROLE_USER = "ROLE_USER";
    private final RSAKeyProperties keys;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, RSAKeyProperties rsaKeyProperties) {

        this.customUserDetailsService = customUserDetailsService;
        this.keys = rsaKeyProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cafeterias/**").hasAuthority(ROLE_OWNER)
                        .requestMatchers(HttpMethod.POST, "/campuses/**").hasAuthority(ROLE_OWNER)
                        .requestMatchers(HttpMethod.POST, "/universities/**").hasAuthority(ROLE_OWNER)
                        .anyRequest().authenticated())
                /*.addFilterBefore(new UserFingerprintFilter(fingerprintService), BearerTokenAuthenticationFilter.class)*/
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withPublicKey(keys.getPublicKey())
                .build();


        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(20)), // valida claims exp e nbf
                new JwtIssuerValidator("cashew-api"), // valida claim "iss"
                new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains("cashew-api")), // valida claim "aud"
                new JwtAlgorithmValidator(),
                new JwtAccessTokenJtiValidator()/*, // valida claim "alg"
                new JwtFingerprintValidator(fingerprintService.getUserFingerprint())*/);

        nimbusJwtDecoder.setJwtValidator(validator);
        return nimbusJwtDecoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey
                .Builder(keys.getPublicKey())
                .algorithm(JWSAlgorithm.RS256)
                .privateKey(keys.getPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public RSAKeyProperties rsaKeyProperties(RSAKeyProperties rsaKeyProperties) {
        rsaKeyProperties.setPublicKey(keys.getPublicKey());
        rsaKeyProperties.setPrivateKey(keys.getPrivateKey());
        return rsaKeyProperties;
    }
}
// TODO adicionar validacao de sub no JwtDecoder
// TODO arrumar UserFingeprintValidator