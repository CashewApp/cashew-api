package br.app.cashew.feature03.cafeteria.exception.university.cnpj;

public class CnpjAlreadyExistsException extends CnpjException {

    public CnpjAlreadyExistsException(String message, String field) {
        super(message, field);
    }
}
