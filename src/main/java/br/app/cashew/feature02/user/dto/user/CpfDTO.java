package br.app.cashew.feature02.user.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class CpfDTO {

    @CPF(message = "CPF invalido")
    private String cpf;
}
