package br.app.cashew.feature02.user.dto.user.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class UserUpdateInfoDTO {

    @Email(message = "Email invalido")
    @NotNull(message = "Email invalido")
    private String email;

    @NotBlank(message = "Nome invalido")
    @Size(max = 40 , message = "Nome invalido. Maximo de 40 caracteres")
    private String name;

    @Size(min = 11, max = 11)
    @CPF(message = "CPF invalido")
    private String cpf;
}