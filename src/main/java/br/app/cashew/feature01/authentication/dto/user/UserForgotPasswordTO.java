package br.app.cashew.feature01.authentication.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserForgotPasswordTO {

    @Email(message = "Email invalido")
    private String email;
}
