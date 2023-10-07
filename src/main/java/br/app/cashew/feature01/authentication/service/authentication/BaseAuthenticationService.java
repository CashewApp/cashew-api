package br.app.cashew.feature01.authentication.service.authentication;

import br.app.cashew.feature01.authentication.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface BaseAuthenticationService<T> {

    public abstract Authentication register(T user);

    public abstract Authentication login(String email, String password);

    Authentication generateAuthentication(Jwt jwt);

    Optional<User> validateEmail(String email);
}
