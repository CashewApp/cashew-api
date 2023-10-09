package br.app.cashew.feature02.user.dto.user.input;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversityUserRequestDTO {

    @NotEmpty(message = "identificador público da universidade inválida")
    private String universityPublicKey;

    @NotEmpty(message = "identificador público do campus inválido")
    private String campusPublicKey;
}
