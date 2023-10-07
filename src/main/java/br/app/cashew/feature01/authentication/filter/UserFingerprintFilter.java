package br.app.cashew.feature01.authentication.filter;

import br.app.cashew.feature01.authentication.service.FingerprintService;
import br.app.cashew.feature01.authentication.exception.fingerprint.FingerprintDoesNotExists;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


// @Component
public class UserFingerprintFilter extends OncePerRequestFilter{

    private final FingerprintService fingerprintService;

    @Autowired
    public UserFingerprintFilter(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        Optional<String> userFingerprint = Optional.ofNullable(request.getHeader("userFingerPrint"));

        if (userFingerprint.isPresent()) {

            fingerprintService.setUserFingerprint(userFingerprint.get());
            chain.doFilter(request, response);
        }
        else {
            throw new FingerprintDoesNotExists("Fingerprint is null");
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String requestPath = request.getRequestURI();

        return requestPath.startsWith("/api/v1/auth");
    }
}
