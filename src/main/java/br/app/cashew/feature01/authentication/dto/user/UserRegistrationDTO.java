package br.app.cashew.feature01.authentication.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationDTO {

    @Size(max = 40 , message = "Nome invalido. Maximo de 40 caracteres")
    @NotBlank(message = "Nome invalido")
    private String name;

    @NotBlank(message = "Email invalido")
    @Email(message = "Email invalido")
    private String email;

    @Size(min = 8, message = "Senha invalida. Minimo de 8 digitos")
    private String password;
}
