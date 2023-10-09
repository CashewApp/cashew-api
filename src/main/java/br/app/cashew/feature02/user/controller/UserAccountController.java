package br.app.cashew.feature02.user.controller;

import br.app.cashew.feature01.authentication.dto.user.UserChangePasswordDTO;
import br.app.cashew.feature01.authentication.service.authentication.user.UserPasswordService;
import br.app.cashew.feature02.user.dto.user.input.UniversityUserRequestDTO;
import br.app.cashew.feature02.user.dto.user.input.UserEmailChangeDTO;
import br.app.cashew.feature02.user.dto.user.input.UserUpdateInfoDTO;
import br.app.cashew.feature02.user.dto.user.output.UniversityUserResponseDTO;
import br.app.cashew.feature02.user.service.UserAccountService;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUser;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public UserAccountController(
            UserAccountService userAccountService,
            UserPasswordService userPasswordService,
            ModelMapper modelMapper) {

        this.userAccountService = userAccountService;
        this.userPasswordService = userPasswordService;
        this.modelMapper = modelMapper;
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

    @PostMapping("/info")
    public ResponseEntity<Void> addCpf(@RequestBody Map<String, String> cpf, Authentication authentication) {
        userAccountService.addCpf(cpf.get("cpf"), authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
        // TODO criar hibernate constraint para validar CPF
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

    @PostMapping("/university_and_campus_preferences")
    public ResponseEntity<UniversityUserResponseDTO> addUniversityAndCampusPreferences(@RequestBody @Valid UniversityUserRequestDTO userAdditionalInfoDTO, Authentication authentication) {
        UniversityUser userPreferences = userAccountService.addUniversityAndCampusPreferences(
                userAdditionalInfoDTO.getUniversityPublicKey(),
                userAdditionalInfoDTO.getCampusPublicKey(),
                authentication.getName());

        UniversityUserResponseDTO universityUserResponseDTO = convertUniversityUserToUniversityUserResponseDTO(userPreferences);
        return new ResponseEntity<>(universityUserResponseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/university_and_campus_preferences")
    public ResponseEntity<List<UniversityUserResponseDTO>> getUniversityAndCampusPreferences(Authentication authentication) {

        List<UniversityUserResponseDTO> userUniversityAndCampusPreferences = userAccountService
                .getUniversityAndCampusPreferences(authentication.getName())
                .stream()
                .map(this::convertUniversityUserToUniversityUserResponseDTO)
                .toList();

        return new ResponseEntity<>(userUniversityAndCampusPreferences, HttpStatus.OK);
    }

    private UniversityUserResponseDTO convertUniversityUserToUniversityUserResponseDTO(UniversityUser universityUser) {
        return modelMapper.map(universityUser, UniversityUserResponseDTO.class);
    }
}
