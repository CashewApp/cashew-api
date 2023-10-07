package br.app.cashew.feature01.authentication.util.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RefreshTokenPropertiesUtility {

    private static final String ISSUER = "cashew-api";
    private static final String FINGERPRINT_CLAIM_NAME = "userFingerprint";
    private static final String JTI_CLAIM_NAME = "jti";

    private RefreshTokenPropertiesUtility() {}

    public static Instant getCurrentTime() {

        return Instant.now();
    }

    public static Instant getExpirationTime(Instant currentTime) {

        return currentTime.plus(2, ChronoUnit.DAYS);
    }

    public static String getFingerprintClaimName() {
        return FINGERPRINT_CLAIM_NAME;
    }

    public static String getJtiClaimName() {
        return JTI_CLAIM_NAME;
    }

    public static String getIssuer() {
        return ISSUER;}
}
