package br.app.cashew.feature01.authentication.util.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AccessTokenPropertiesUtility {

    private AccessTokenPropertiesUtility(){}

    private static final String ROLES_CLAIM_NAME = "roles";
    private static final String FINGERPRINT_CLAIM_NAME = "userFingerprint";
    private static final String ISSUER = "cashew-api";
    private static final List<String> AUDIENCES = new ArrayList<>(List.of("cashew-api"));

    public static Instant getCurrentTime() {

        return Instant.now();
    }

    public static Instant getExpirationTime(Instant curretTime) {

        return curretTime.plus(15, ChronoUnit.MINUTES);
    }

    public static List<String> getAudiences() {

        return AUDIENCES;
    }

    public static List<String> getRoles(Authentication authentication) {

        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
    }

    public static String getIssuer() {
        return ISSUER;
    }

    public static String getRolesClaimName() {
        return ROLES_CLAIM_NAME;
    }

    public static String getFingerprintClaimName() {
        return FINGERPRINT_CLAIM_NAME;
    }
}
