package br.app.cashew.feature01.authentication.service.jwt;

import br.app.cashew.feature01.authentication.exception.RefreshTokenIsInvalidException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtPartnerSubjectValidator;
import br.app.cashew.feature01.authentication.service.jwt.validators.JwtRefreshTokenJtiValidator;
import com.nimbusds.jose.util.Base64URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kms.KmsClient;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service("partnerJwtService")
public class PartnerJwtService extends BaseJwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PartnerRepository partnerRepository;

    @Autowired
    public PartnerJwtService(JwtDecoder jwtDecoder,
                             KmsClient kmsClient,
                             RefreshTokenRepository refreshTokenRepository,
                             PartnerRepository partnerRepository) {
        super(jwtDecoder, kmsClient);
        this.refreshTokenRepository = refreshTokenRepository;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String userFingerprint) {
        RefreshToken token = new RefreshToken();

        Optional<Partner> partner = partnerRepository.findByPartnerPublicKey(UUID.fromString(authentication.getName()));

        if (partner.isEmpty()) {
            throw new UserDoesNotExistsException("User is invalid");
        }

        token.setPartner(partner.get());
        token.setJti(UUID.randomUUID());
        refreshTokenRepository.save(token);
        String jwtHeader = super.generateJwtHeader();
        String jwtPayload = generateRefreshTokenJwtPayload(authentication, userFingerprint, token);
        byte[] signature = super.getSignature(jwtHeader, jwtPayload);
        return Base64URL.encode(jwtHeader) + "." + Base64URL.encode(jwtPayload) + "." + Base64URL.encode(signature);
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
