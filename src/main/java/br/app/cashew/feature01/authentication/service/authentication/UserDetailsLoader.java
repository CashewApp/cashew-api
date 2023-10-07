package br.app.cashew.feature01.authentication.service.authentication;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsLoader {

    UserDetails loadUserByUsername(String email);
}
