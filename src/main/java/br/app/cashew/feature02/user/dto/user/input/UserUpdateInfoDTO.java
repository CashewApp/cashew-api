package br.app.cashew.feature02.user.dto.user.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateInfoDTO {

    @Email(message = "Email invalido")
    @NotNull(message = "Email invalido")
    private String email;

    @NotBlank(message = "Nome invalido")
    @Size(max = 40 , message = "Nome invalido. Maximo de 40 caracteres")
    private String name;

    @Size(min = 11, max = 11)
    private String cpf;
}

// LEMBRETES front end tem q adicionar mascara de email e tem q fazer o get das informacoes antes p cache
