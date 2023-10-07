package br.app.cashew.feature01.authentication.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    @NotBlank(message = "Email invalido")
    @Email(message = "Email invalido")
    private String email;

    private String password;
}
