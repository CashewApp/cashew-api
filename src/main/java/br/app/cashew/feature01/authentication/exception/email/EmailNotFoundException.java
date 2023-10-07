package br.app.cashew.feature01.authentication.exception.email;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.OK)
public class EmailNotFoundException extends AuthenticationException {

    private String field;

    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotFoundException(String message, String field) {
        super(message);
        this.field = field;
    }
}
