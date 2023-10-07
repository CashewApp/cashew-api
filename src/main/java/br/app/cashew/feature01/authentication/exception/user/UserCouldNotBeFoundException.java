package br.app.cashew.feature01.authentication.exception.user;

public class UserCouldNotBeFoundException extends RuntimeException {
    public UserCouldNotBeFoundException(String message) {
        super(message);
    }
}
