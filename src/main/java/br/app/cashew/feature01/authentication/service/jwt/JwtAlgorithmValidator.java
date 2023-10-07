package br.app.cashew.feature01.authentication.service.jwt;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class JwtAlgorithmValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("Error in \"alg\" claim in Header", "\"alg\" claim is not valid ", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {


        Optional<String> algClaim = Optional.ofNullable( (String) token.getHeaders().get("alg"));

        if (algClaim.isEmpty() || !(algClaim.get().equals("RS256"))) {
            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();

    }
}
