package br.app.cashew.feature01.authentication.config;

import br.app.cashew.feature01.authentication.service.authentication.CustomUserDetailsService;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtAccessTokenJtiValidator;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtAlgorithmValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.AUD;

@Configuration
@EnableAsync
public class SecurityConfig {

    @Value("${.kms.publickey}")
    private String publickey;
    private static final String ROLE_OWNER = "ROLE_OWNER";
    private static final String ROLE_USER = "ROLE_USER";
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {

        this.customUserDetailsService = customUserDetailsService;
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

        NimbusJwtDecoder nimbusJwtDecoder;
        try {
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withPublicKey(getRSAPublicKeyFromPEM())
                    .build();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

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

    private RSAPublicKey getRSAPublicKeyFromPEM() throws NoSuchAlgorithmException, InvalidKeySpecException {

        publickey = publickey.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publickey = publickey.replace("-----END PUBLIC KEY-----", "");
        publickey = publickey.replaceAll("\\s", "");
        X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publickey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
// TODO adicionar validacao de sub no JwtDecoder
// TODO arrumar UserFingeprintValidator