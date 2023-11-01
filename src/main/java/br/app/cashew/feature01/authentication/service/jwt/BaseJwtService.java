package br.app.cashew.feature01.authentication.service.jwt;

import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.util.token.AccessTokenPropertiesUtility;
import br.app.cashew.feature01.authentication.util.token.RefreshTokenPropertiesUtility;
import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.MessageType;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public abstract class BaseJwtService {

    @Value("${.kms.keyid}")
    private String keyId;
    private final JwtDecoder jwtDecoder;
    private final KmsClient kmsClient;

    @Autowired
    protected BaseJwtService(JwtDecoder jwtDecoder, KmsClient kmsClient) {

        this.jwtDecoder = jwtDecoder;
        this.kmsClient = kmsClient;
    }

    public abstract String generateRefreshToken(Authentication authentication, String userFingerprint);

    public abstract boolean validateRefreshToken(Jwt refreshToken);

    public String generateAccessToken(Authentication authentication, String userFingerprint) {
        String jwtPayload = generateAccessTokenJwtPayload(authentication, userFingerprint);
        String jwtHeader = generateJwtHeader();
        byte[] signature = getSignature(jwtHeader, jwtPayload);
        return Base64URL.encode(jwtHeader) + "." + Base64URL.encode(jwtPayload) + "." + Base64URL.encode(signature);
    }

    private String generateAccessTokenJwtPayload(Authentication authentication, String userFingerPrint) {

        Date currentTime = AccessTokenPropertiesUtility.getCurrentTime();
        Date expirationTime = AccessTokenPropertiesUtility.getExpirationTime(currentTime);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer(AccessTokenPropertiesUtility.getIssuer()) // refatorar para ser uma constante em uma classe
                .issueTime(currentTime)
                .expirationTime(expirationTime)
                .notBeforeTime(currentTime)
                .audience(AccessTokenPropertiesUtility.getAudiences())
                .subject(authentication.getName())
                /*.claim("sub_jwk", rsaKeyProperties.getPublicKey().toString())*/
                .claim(AccessTokenPropertiesUtility.getRolesClaimName(), AccessTokenPropertiesUtility.getRoles(authentication))
                .claim(AccessTokenPropertiesUtility.getFingerprintClaimName(), TokenGeneratorUtility.hashToken(userFingerPrint))
                .build();

        JSONObject jsonObject = new JSONObject(jwtClaimsSet.toJSONObject());
        return jsonObject.toString();
    }

    public String generateRefreshTokenJwtPayload(Authentication authentication, String userFingerprint, RefreshToken token) {

        Date currentTime = AccessTokenPropertiesUtility.getCurrentTime();
        Date expirationTime = AccessTokenPropertiesUtility.getExpirationTime(currentTime);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(currentTime)
                .expirationTime(expirationTime)
                .subject(authentication.getName())
                /*.claim("sub_jwk", rsaKeyProperties.getPublicKey().toString())*/
                .claim(RefreshTokenPropertiesUtility.getJtiClaimName(), token.getJti())
                .claim(RefreshTokenPropertiesUtility.getFingerprintClaimName(), TokenGeneratorUtility.hashToken(userFingerprint))
                .build();

        JSONObject jsonObject = new JSONObject(jwtClaimsSet.toJSONObject());
        return jsonObject.toString();
    }

    public Jwt extractRefreshToken(String refreshToken) {
        return jwtDecoder.decode(refreshToken);
    }

    public String generateJwtHeader() {
        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        JSONObject jsonObject = new JSONObject(jwsHeader.toJSONObject());
        return jsonObject.toString();
    }

    public byte[] getSignature(String jwtHeader, String jwtPayload) {

        SdkBytes sdkBytes = SdkBytes.fromString(Base64URL.encode(jwtHeader) + "." + Base64URL.encode(jwtPayload), StandardCharsets.UTF_8);

        // Criar um SignRequest
        SignRequest signRequest = SignRequest.builder()
                .keyId(keyId)
                .messageType(MessageType.RAW)
                .signingAlgorithm(SigningAlgorithmSpec.RSASSA_PKCS1_V1_5_SHA_256)
                .message(sdkBytes)
                .build();

        return kmsClient.sign(signRequest).signature().asByteArray();
    }
}
