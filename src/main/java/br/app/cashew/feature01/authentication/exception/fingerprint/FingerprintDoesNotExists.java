package br.app.cashew.feature01.authentication.exception.fingerprint;

import org.springframework.security.core.AuthenticationException;

public class FingerprintDoesNotExists extends AuthenticationException {
    public FingerprintDoesNotExists(String message) {
        super(message);
    }

    public FingerprintDoesNotExists(String message, Throwable cause) {
        super(message, cause);
    }
}
