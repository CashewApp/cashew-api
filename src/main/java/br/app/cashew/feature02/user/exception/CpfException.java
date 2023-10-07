package br.app.cashew.feature02.user.exception;

import br.app.cashew.global.exception.GenericException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfException extends GenericException {

    public CpfException(String message, String field) {
        super(message, field);
    }
}
