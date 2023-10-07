package br.app.cashew.feature03.cafeteria.exception.university;

import br.app.cashew.global.exception.GenericException;

public class UniversityAlreadyExistsException extends GenericException {

    public UniversityAlreadyExistsException(String message, String field) {
        super(message, field);
    }
}
