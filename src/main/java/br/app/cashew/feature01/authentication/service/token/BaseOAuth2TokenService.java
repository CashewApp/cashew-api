package br.app.cashew.feature01.authentication.service.token;

import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.util.key.RSAKeyProperties;
import br.app.cashew.feature01.authentication.util.token.AccessTokenPropertiesUtility;
import br.app.cashew.feature01.authentication.util.token.RefreshTokenPropertiesUtility;
import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public abstract class BaseOAuth2TokenService {

    private final JwtEncoder jwtEncoder;
    private final RSAKeyProperties rsaKeyProperties;

    @Autowired
    protected BaseOAuth2TokenService(JwtEncoder jwtEncoder, RSAKeyProperties rsaKeyProperties) {

        this.jwtEncoder = jwtEncoder;
        this.rsaKeyProperties = rsaKeyProperties;
    }

    public abstract String generateRefreshToken(Authentication authentication, String userFingerprint);

    public abstract boolean validateRefreshToken(Jwt refreshToken);

    public Jwt extractRefreshToken(String refreshToken) {

        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey())
                .build();

        return jwtDecoder.decode(refreshToken);
    }

    public String generateAccessToken(Authentication authentication, String userFingerprint) {

        Instant currentTime = AccessTokenPropertiesUtility.getCurrentTime();
        Instant expirationTime = AccessTokenPropertiesUtility.getExpirationTime(currentTime);

        // Cria o payload com as devidas claims
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(AccessTokenPropertiesUtility.getIssuer()) // refatorar para ser uma constante em uma classe
                .issuedAt(currentTime) // refatorar para ser uma constante em uma classe
                .expiresAt(expirationTime) // refatorar para ser uma constante em uma classe
                .notBefore(currentTime) // refatorar para ser uma constante em uma classe
                .audience(AccessTokenPropertiesUtility.getAudiences())
                .subject(authentication.getName())
                /*.claim("sub_jwk", rsaKeyProperties.getPublicKey().toString())*/
                .claim(AccessTokenPropertiesUtility.getRolesClaimName(), AccessTokenPropertiesUtility.getRoles(authentication))
                .claim(AccessTokenPropertiesUtility.getFingerprintClaimName(), TokenGeneratorUtility.hashToken(userFingerprint))
                .build();


        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String encodeRefreshToken(Authentication authentication, String userFingerprint, Instant currentTime, RefreshToken token) {

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .expiresAt(RefreshTokenPropertiesUtility.getExpirationTime(currentTime))
                .subject(authentication.getName())
                .claim(RefreshTokenPropertiesUtility.getJtiClaimName(), token.getJti())
                .claim(RefreshTokenPropertiesUtility.getFingerprintClaimName(), TokenGeneratorUtility.hashToken(userFingerprint))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
