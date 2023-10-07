package br.app.cashew.feature03.cafeteria.exception.university.cnpj;

public class CnpjInvalidException extends CnpjException{
    public CnpjInvalidException(String message, String field) {
        super(message, field);
    }
}
