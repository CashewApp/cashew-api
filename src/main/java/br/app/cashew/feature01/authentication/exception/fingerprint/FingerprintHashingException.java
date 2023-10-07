package br.app.cashew.feature01.authentication.exception.fingerprint;

public class FingerprintHashingException extends RuntimeException{

    public FingerprintHashingException(String message) {
        super(message);
    }

    public FingerprintHashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
