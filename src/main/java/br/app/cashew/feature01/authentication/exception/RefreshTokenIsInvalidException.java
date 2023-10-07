package br.app.cashew.feature01.authentication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenIsInvalidException extends RuntimeException {

    private String field;

    public RefreshTokenIsInvalidException() {
        super();
    }

    public RefreshTokenIsInvalidException(String message, String field) {
        super(message);
        this.field = field;
    }

    public RefreshTokenIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
