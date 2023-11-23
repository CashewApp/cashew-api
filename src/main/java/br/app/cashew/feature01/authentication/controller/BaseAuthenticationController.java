package br.app.cashew.feature01.authentication.controller;

import br.app.cashew.feature01.authentication.dto.RefreshTokenDTO;
import br.app.cashew.feature01.authentication.dto.user.UserLoginDTO;
import br.app.cashew.feature01.authentication.exception.RefreshTokenIsInvalidException;
import br.app.cashew.feature01.authentication.service.authentication.BaseAuthenticationService;
import br.app.cashew.feature01.authentication.service.jwt.BaseJwtService;
import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class BaseAuthenticationController<T> {

    private final BaseJwtService baseJwtService;
    protected static final String ACCESS_TOKEN_PROPERTY_NAME = "access_token";
    protected static final String REFRESH_TOKEN_PROPERTY_NAME = "refresh_token";
    protected static final String USER_FINGERPRINT_PROPERTY_NAME = "userFingerprint";
    protected static final String HEADER_REPRESENTATION_PROPERTY_NAME = "header";
    protected static final String MESSAGE_PROPERTY_NAME = "message";

    @Autowired
    protected BaseAuthenticationController(BaseJwtService baseJwtService) {

        this.baseJwtService = baseJwtService;
    }

    public ResponseEntity<Map<String, Object>> registrate(@RequestBody @Valid T userRegistrationDTO) {

        BaseAuthenticationService<T> authenticationService = getAuthenticationService();

        Authentication authentication = authenticationService.register(userRegistrationDTO);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> response = generateResponse(authentication);
        HttpHeaders httpHeaders = (HttpHeaders) response.get(HEADER_REPRESENTATION_PROPERTY_NAME);
        response.remove(HEADER_REPRESENTATION_PROPERTY_NAME);

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.CREATED);
    }


    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        BaseAuthenticationService<T> authenticationService = getAuthenticationService();

        Authentication authentication = authenticationService.login(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> response = generateResponse(authentication);
        HttpHeaders httpHeaders = (HttpHeaders) response.get(HEADER_REPRESENTATION_PROPERTY_NAME);
        response.remove(HEADER_REPRESENTATION_PROPERTY_NAME);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    public abstract BaseAuthenticationService<T> getAuthenticationService();

    public ResponseEntity<Map<String, Object>> getNewRefreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {

        Jwt refreshToken = baseJwtService.extractRefreshToken(refreshTokenDTO.getRefresh_token());

        if ((baseJwtService.validateRefreshToken(refreshToken))) {
            throw new RefreshTokenIsInvalidException("Refresh token is invalid", REFRESH_TOKEN_PROPERTY_NAME);
        }

        BaseAuthenticationService<T> authenticationService = getAuthenticationService();

        Authentication authentication = authenticationService.generateAuthentication(refreshToken);

        Map<String, Object> response = generateResponse(authentication);
        HttpHeaders httpHeaders = (HttpHeaders) response.get(HEADER_REPRESENTATION_PROPERTY_NAME);
        response.remove(HEADER_REPRESENTATION_PROPERTY_NAME);

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    public Map<String, Object> generateResponse(Authentication authentication) {

        String userFingerprint = TokenGeneratorUtility.generateToken();

        String accessToken = baseJwtService.generateAccessToken(authentication, userFingerprint);
        String refreshToken = baseJwtService.generateRefreshToken(authentication, userFingerprint);

        Map<String, Object> response = new HashMap<>();
        response.put(ACCESS_TOKEN_PROPERTY_NAME, accessToken);
        response.put(REFRESH_TOKEN_PROPERTY_NAME, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_FINGERPRINT_PROPERTY_NAME, userFingerprint);
        response.put(HEADER_REPRESENTATION_PROPERTY_NAME, headers);

        return response;
    }
}
