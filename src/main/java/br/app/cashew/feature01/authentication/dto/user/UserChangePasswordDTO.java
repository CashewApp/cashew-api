package br.app.cashew.feature01.authentication.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserChangePasswordDTO {

    private String oldPassword;

    @NotBlank(message = "Senha invalida")
    @Size(min = 8, message = "Senha invalida. Minimo de 8 digitos")
    private String newPassword;
}
