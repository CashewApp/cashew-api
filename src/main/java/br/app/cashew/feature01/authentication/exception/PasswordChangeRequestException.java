package br.app.cashew.feature01.authentication.exception;

public class PasswordChangeRequestException extends RuntimeException {

    public PasswordChangeRequestException() {
        super();
    }

    public PasswordChangeRequestException(String message) {
        super(message);
    }

    public PasswordChangeRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
