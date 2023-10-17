package br.app.cashew.feature03.cafeteria.exception;

public class CafeteriaDoesNotExistsException extends RuntimeException {
    
    public CafeteriaDoesNotExistsException(String message) {
        super(message);
    }

    
}
