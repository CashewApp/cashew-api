package br.app.cashew.global.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException{

    public final String field;

    public GenericException(String message, String field) {
        super(message);
        this.field = field;
    }
}
