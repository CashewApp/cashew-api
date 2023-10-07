package br.app.cashew.feature01.authentication.service.authentication.user;

import br.app.cashew.feature01.authentication.exception.email.EmailNotFoundException;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import br.app.cashew.feature01.authentication.service.authentication.UserDetailsLoader;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsLoaderForUser implements UserDetailsLoader {

    private final UserRepository userRepository;

    public UserDetailsLoaderForUser(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EmailNotFoundException("Não foi possível encontrar sua conta do Cashew", "email"));

        Set<GrantedAuthority> roles = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUserPublicKey().toString(),
                user.getPassword(),
                roles
        );
    }
}
