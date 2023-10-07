package br.app.cashew.feature03.cafeteria.exception;

public class CafeteriaDoesNotExistException extends RuntimeException {
    
    public CafeteriaDoesNotExistException(String message) {
        super(message);
    }

    
}
