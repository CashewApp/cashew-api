package br.app.cashew.feature03.cafeteria.exception.university.cnpj;

import br.app.cashew.global.exception.GenericException;

public class CnpjException extends GenericException {
    public CnpjException(String message, String field) {
        super(message, field);
    }
}
