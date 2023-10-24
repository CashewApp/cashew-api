package br.app.cashew.feature01.authentication.service.jwt.validators;

import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@NoArgsConstructor
public class JwtPartnerSubjectValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("Error in \"sub\" claim in payload", "\"sub\" claim is invalid ", null);
    private PartnerRepository partnerRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtPartnerSubjectValidator(PartnerRepository partnerRepository, RefreshTokenRepository refreshTokenRepository) {

        this.partnerRepository = partnerRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        UUID subCLaim = UUID.fromString(token.getClaim("sub"));

        // se o sub possui um refresh token de mesmo jti igual ao passado, entao valido
        Partner partner = partnerRepository.findByPartnerPublicKey(subCLaim)
                .orElseThrow(
                        () -> new UserDoesNotExistsException("User is invalid"));

        if (refreshTokenRepository.existsRefreshTokenByJtiAndPartner(subCLaim, partner)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}
