package br.app.cashew.feature01.authentication.service.token;

import br.app.cashew.feature01.authentication.exception.RefreshTokenIsInvalidException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import br.app.cashew.feature01.authentication.service.jwt.JwtPartnerSubjectValidator;
import br.app.cashew.feature01.authentication.service.jwt.JwtRefreshTokenJtiValidator;
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

@Service("partnerOAuth2TokenServiceImpl")
public class PartnerOAuth2TokenServiceImpl extends BaseOAuth2TokenService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final PartnerRepository partnerRepository;

    @Autowired
    public PartnerOAuth2TokenServiceImpl(JwtEncoder jwtEncoder,
                                         RSAKeyProperties rsaKeyProperties,
                                         RefreshTokenRepository refreshTokenRepository,
                                         PartnerRepository partnerRepository) {
        super(jwtEncoder, rsaKeyProperties);
        this.refreshTokenRepository = refreshTokenRepository;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String userFingerprint) {

        Instant currentTime = RefreshTokenPropertiesUtility.getCurrentTime();

        RefreshToken token = new RefreshToken();

        Optional<Partner> partner = partnerRepository.findByPartnerPublicKey(UUID.fromString(authentication.getName()));

        if (partner.isEmpty()) {
            throw new UserDoesNotExistsException("User is invalid");
        }

        token.setPartner(partner.get());
        token.setJti(UUID.randomUUID());
        refreshTokenRepository.save(token);

        return encodeRefreshToken(authentication, userFingerprint, currentTime, token);
    }

    @Override
    public boolean validateRefreshToken(Jwt refreshToken) {

        OAuth2TokenValidatorResult result;

        try {
            OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                    new JwtTimestampValidator(Duration.ofSeconds(20)),
                    new JwtRefreshTokenJtiValidator(refreshTokenRepository),
                    new JwtPartnerSubjectValidator(partnerRepository, refreshTokenRepository));
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
