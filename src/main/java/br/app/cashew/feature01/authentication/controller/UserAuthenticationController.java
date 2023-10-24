package br.app.cashew.feature01.authentication.controller;


import br.app.cashew.feature01.authentication.dto.RefreshTokenDTO;
import br.app.cashew.feature01.authentication.dto.user.UserChangeForgetPasswordDTO;
import br.app.cashew.feature01.authentication.dto.user.UserForgotPasswordTO;
import br.app.cashew.feature01.authentication.dto.user.UserLoginDTO;
import br.app.cashew.feature01.authentication.dto.user.UserRegistrationDTO;
import br.app.cashew.feature01.authentication.exception.PasswordChangeRequestException;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.service.authentication.BaseAuthenticationService;
import br.app.cashew.feature01.authentication.service.authentication.user.UserAuthenticationServiceImpl;
import br.app.cashew.feature01.authentication.service.authentication.user.UserPasswordService;
import br.app.cashew.feature01.authentication.service.jwt.BaseJwtService;
import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import br.app.cashew.feature02.user.service.email.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/user")
public class UserAuthenticationController extends BaseAuthenticationController<UserRegistrationDTO>{
    private final UserAuthenticationServiceImpl userAuthenticationServiceImpl;
    private final EmailService emailService;
    private final UserPasswordService userPasswordService;


    @Autowired
    public UserAuthenticationController(
            UserAuthenticationServiceImpl userAuthenticationServiceImpl,
            @Qualifier("userJwtService") BaseJwtService baseOAuth2TokenService,
            EmailService emailService,
            UserPasswordService userPasswordService) {

        super(baseOAuth2TokenService);
        this.userAuthenticationServiceImpl = userAuthenticationServiceImpl;
        this.emailService = emailService;
        this.userPasswordService = userPasswordService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> registrateUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {

        return super.registrate(userRegistrationDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        return super.login(userLoginDTO);
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getNewUserRefreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {

        return super.getNewRefreshToken(refreshTokenDTO);
    }

    @PostMapping("/password-change-requests")
    public ResponseEntity<Map<String, String>> createPasswordChangeRequest(@RequestBody @Valid UserForgotPasswordTO userForgotPasswordTO) {

        // chama servico que valida o email
        Optional<User> user = userAuthenticationServiceImpl.validateEmail(userForgotPasswordTO.getEmail());

        if (user.isEmpty()) {
            Map<String, String> response = Collections.
                    singletonMap(MESSAGE_PROPERTY_NAME, "Caso este e-mail exista em nosso sistema, uma mensagem foi enviada para o e-mail informado!");

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

        if (!userPasswordService.validatePasswordChangeRequestCreationAttempt(user.get())) {

            Map<String, String> response = Collections.
                    singletonMap(MESSAGE_PROPERTY_NAME, "Uma tentativa ja foi feita a menos de 24 horas. Tente novamente mais tarde");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String token = userPasswordService.generatePasswordChangeRequest(user.get());

        try {
            emailService.sendForgotPasswordEmail(userForgotPasswordTO.getEmail(), token);
        }
        catch (Exception e) {
            System.out.println((Arrays.toString(e.getStackTrace())));
        }

        Map<String, String> response = Collections.
                singletonMap(MESSAGE_PROPERTY_NAME, "Caso este e-mail exista em nosso sistema, uma mensagem foi enviada para o e-mail informado!");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/password")
    public ResponseEntity<String> changePassword(@RequestParam String token, @RequestBody @Valid UserChangeForgetPasswordDTO userChangeForgetPasswordDTO) {

        String hashedPasswordChangeRequestToken = TokenGeneratorUtility.hashToken(token);

        // valida o token do password change request
        boolean isPasswordChangeRequestValid = userPasswordService.validatePasswordChangeRequest(hashedPasswordChangeRequestToken);

        if (!isPasswordChangeRequestValid) {
            throw new PasswordChangeRequestException("Solicitacao de mudanca de senha invalida");
        }

        User user = userPasswordService.getUserByPasswordChangeRequest(hashedPasswordChangeRequestToken);
        userPasswordService.invalidatePasswordChangeRequest(hashedPasswordChangeRequestToken);
        userPasswordService.changePassword(userChangeForgetPasswordDTO.getNewPassword(), user.getUserPublicKey());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseAuthenticationService<UserRegistrationDTO> getAuthenticationService() {
        return userAuthenticationServiceImpl;
    }
}
// TODO adicionar armazenamento das chaves criptograficas a serem feitas pelo AWS kms
// TODO adicionar confirmacao de e-mail durante a criacao da conta