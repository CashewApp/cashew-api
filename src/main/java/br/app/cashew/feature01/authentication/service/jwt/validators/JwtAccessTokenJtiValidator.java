package br.app.cashew.feature01.authentication.service.jwt.validators;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtAccessTokenJtiValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("Error in \"jti\" claim in payload", "\"jti\" claim is not null when it must be null", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        String jtiClaim = token.getClaim("jti");

        if (jtiClaim != null) {
            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}
