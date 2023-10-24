package br.app.cashew.feature01.authentication.service.jwt;

import br.app.cashew.feature01.authentication.exception.RefreshTokenIsInvalidException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtRefreshTokenJtiValidator;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtUserSubjectValidator;
import br.app.cashew.feature01.authentication.util.key.RSAKeyProperties;
import br.app.cashew.feature01.authentication.util.token.RefreshTokenPropertiesUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service("userJwtService")
public class UserJwtService extends BaseJwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserJwtService(JwtEncoder jwtEncoder,
                          RSAKeyProperties rsaKeyProperties,
                          RefreshTokenRepository refreshTokenRepository,
                          UserRepository userRepository) {
        super(jwtEncoder, rsaKeyProperties);
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String userFingerprint) {

        Instant currentTime = RefreshTokenPropertiesUtility.getCurrentTime();

        RefreshToken token = new RefreshToken();

        Optional<User> user = userRepository.findByUserPublicKey(UUID.fromString(authentication.getName()));

        if (user.isEmpty()) {
            throw new UserDoesNotExistsException("User is invalid");
        }

        token.setUser(user.get());
        token.setJti(UUID.randomUUID());
        refreshTokenRepository.save(token);
        token.setUser(user.get());
        return super.encodeRefreshToken(authentication, userFingerprint, currentTime, token);
    }

    @Override
    public boolean validateRefreshToken(Jwt refreshToken) {

        OAuth2TokenValidatorResult result;

        try {
            OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                    new JwtTimestampValidator(Duration.ofSeconds(20)),
                    new JwtRefreshTokenJtiValidator(refreshTokenRepository),
                    new JwtUserSubjectValidator(userRepository, refreshTokenRepository));
            // TODO adicionar validacao do fingerprint

            result = validator.validate(refreshToken);
        }
        catch (JwtValidationException e) {
            throw new RefreshTokenIsInvalidException("Refresh token invalido", "refresh token");
        }

        UUID jti = UUID.fromString(refreshToken.getClaim("jti"));
        refreshTokenRepository.removeByJti(jti);

        return !(result.hasErrors());
    }
}
