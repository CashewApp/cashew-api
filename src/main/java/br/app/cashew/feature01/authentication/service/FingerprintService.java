package br.app.cashew.feature01.authentication.service;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FingerprintService {

    private String userFingerprint;

    public FingerprintService() {}

    public void setUserFingerprint(String userFingerprint) {
        this.userFingerprint = userFingerprint;
    }
}