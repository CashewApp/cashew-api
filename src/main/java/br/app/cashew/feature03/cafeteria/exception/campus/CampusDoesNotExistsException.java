package br.app.cashew.feature03.cafeteria.exception.campus;

import br.app.cashew.global.exception.GenericException;

public class CampusDoesNotExistsException extends GenericException {
    public CampusDoesNotExistsException(String message, String field) {
        super(message, field);
    }
}
