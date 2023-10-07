package br.app.cashew.feature03.cafeteria.exception.campus;

import br.app.cashew.global.exception.GenericException;

public class CampusAlreadyExistsException extends GenericException {

    public CampusAlreadyExistsException(String message, String field) {
        super(message, field);
    }
}
