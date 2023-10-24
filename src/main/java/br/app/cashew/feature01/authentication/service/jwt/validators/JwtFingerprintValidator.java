package br.app.cashew.feature01.authentication.service.jwt.validators;

import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtFingerprintValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("userFingerprint error", "UserFingerprint in header is not valid", null);
    private final String userFingerprint;

    public JwtFingerprintValidator(String userFingerprint) {

        this.userFingerprint = userFingerprint;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        // pegar o fingerprint do header com o FingerPrintService, pegar a claim "userFingerprint" e passar para o FingerPrintGeneratorUtility, se valido entao sucesso

        if (TokenGeneratorUtility.validateToken(userFingerprint, token.getClaimAsString("userFingerprint"))) {
            return OAuth2TokenValidatorResult.success();
        }
        else {
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}
