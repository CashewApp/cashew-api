package br.app.cashew.feature03.cafeteria.exception.university;

import br.app.cashew.global.exception.GenericException;

public class UniversityDoesNotExistsException extends GenericException {

    public UniversityDoesNotExistsException(String message, String field) {
        super(message, field);
    }
}
