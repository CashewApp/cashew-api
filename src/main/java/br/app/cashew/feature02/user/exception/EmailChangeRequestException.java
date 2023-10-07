package br.app.cashew.feature02.user.exception;

import lombok.Getter;

@Getter
public class EmailChangeRequestException extends RuntimeException {

    private String field;

    public EmailChangeRequestException(String message, String field) {
        super(message);
        this.field = field;
    }
}
