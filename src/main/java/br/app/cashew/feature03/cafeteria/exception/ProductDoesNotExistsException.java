package br.app.cashew.feature03.cafeteria.exception;

import br.app.cashew.global.exception.GenericException;

public class ProductDoesNotExistsException extends GenericException {

    public ProductDoesNotExistsException(String message, String field) {
        super(message, field);
    }
}
