package br.app.cashew.feature01.authentication.service.jwt.validators;

import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@NoArgsConstructor
public class JwtRefreshTokenJtiValidator implements OAuth2TokenValidator<Jwt> {

    private RefreshTokenRepository refreshTokenRepository;

    OAuth2Error error = new OAuth2Error("Error in \"jti\" claim in payload", "\"jti\" value is invalid ", null);

    public JwtRefreshTokenJtiValidator(RefreshTokenRepository refreshTokenRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
    }



    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        String claim = token.getClaim("jti");

        UUID jti = UUID.fromString(claim);

        if (refreshTokenRepository.existsByJti(jti)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(error);
    }
}

// TODO adicionar tambem validacao da informação isUsed, se usado, return failure
