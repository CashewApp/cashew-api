package br.app.cashew.feature01.authentication.service.jwt;

import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@NoArgsConstructor
public class JwtUserSubjectValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("Error in \"sub\" claim in payload", "\"sub\" claim is invalid ", null);

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtUserSubjectValidator(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        UUID subCLaim = UUID.fromString(token.getClaim("sub"));

        // se o sub possui um refresh token de mesmo jti igual ao passado, entao valido
        User user = userRepository.findByUserPublicKey(subCLaim)
                .orElseThrow(
                        () -> new UserDoesNotExistsException("User is invalid"));

        if (refreshTokenRepository.existsRefreshTokenByJtiAndUser(subCLaim, user)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(error);
    }
}
