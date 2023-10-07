package br.app.cashew.feature02.user.controller;

import br.app.cashew.feature02.user.dto.user.UserEmailChangeDTO;
import br.app.cashew.feature02.user.dto.user.UserUpdateInfoDTO;
import br.app.cashew.feature01.authentication.dto.user.UserChangePasswordDTO;
import br.app.cashew.feature01.authentication.service.authentication.user.UserPasswordService;
import br.app.cashew.feature02.user.service.UserAccountService;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUser;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users/me/account")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserPasswordService userPasswordService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, UserPasswordService userPasswordService) {

        this.userAccountService = userAccountService;
        this.userPasswordService = userPasswordService;
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, List<String>>> changePassword(@RequestBody @Valid UserChangePasswordDTO userChangePasswordDTO, Authentication authentication) {

        Map<String, List<String>> map = new HashMap<>();
        UUID userPublicKey = UUID.fromString(authentication.getName());

        boolean isPasswordValid = userPasswordService.validateOldPassword(userChangePasswordDTO.getOldPassword(), userPublicKey);

        if (!isPasswordValid) {

            map.put("errors", Collections.singletonList("Senha passada esta incorreta"));
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        userPasswordService.changePassword(userChangePasswordDTO.getNewPassword(), userPublicKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<String> updateNameAndCpfAndEmail(@RequestBody @Valid UserUpdateInfoDTO userUpdateInfoDTO, Authentication authentication) {

        if (userAccountService.updateNameAndCpfAndEmail(userUpdateInfoDTO, authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo(Authentication authentication) {

        // retorna email, nome, cpf
        Map<String, String> info = userAccountService.getUserInfo(authentication.getName());

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<Void> confirmEmailChange(@RequestParam String pin, @RequestBody UserEmailChangeDTO userEmailChangeDTO, Authentication authentication) {

        UUID userPublicKey = UUID.fromString(authentication.getName());

        if (!(userAccountService.validateEmailChangeRequest(pin, userPublicKey))) {

            return new ResponseEntity<>(HttpStatus.GONE);
        }

        userAccountService.updateEmail(userPublicKey, userEmailChangeDTO.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/university_and_campus")
    public ResponseEntity<List<UniversityUser>> getUniversityAndCampusPreferences(Authentication authentication) {

        List<UniversityUser> userUniversityAndCampusPreferences = userAccountService.getUniversityAndCampusPreferences(authentication.getName());

        return new ResponseEntity<>(userUniversityAndCampusPreferences, HttpStatus.OK);
    }
}
