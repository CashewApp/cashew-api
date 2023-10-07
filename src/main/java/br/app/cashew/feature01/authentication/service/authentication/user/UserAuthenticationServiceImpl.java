package br.app.cashew.feature01.authentication.service.authentication.user;

import br.app.cashew.feature01.authentication.exception.email.EmailAlreadyExistsException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.Role;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.dto.user.UserRegistrationDTO;
import br.app.cashew.feature01.authentication.repository.RoleRepository;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import br.app.cashew.feature01.authentication.service.authentication.BaseAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("userAuthenticationServiceImpl")
public class UserAuthenticationServiceImpl implements BaseAuthenticationService<UserRegistrationDTO> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthenticationServiceImpl(UserRepository userRepository,
                                         RoleRepository roleRepository,
                                         AuthenticationManager authenticationManager,
                                         PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication register(UserRegistrationDTO userRegistrationDTO) {

        boolean userExists = userRepository.existsUserByEmail(userRegistrationDTO.getEmail());

        if (userExists) {
            throw new EmailAlreadyExistsException("Este e-mail já está em uso. Tente novamente", "email");
        }
        User user = new User(
                userRegistrationDTO.getName(),
                userRegistrationDTO.getEmail(),
                passwordEncoder.encode(userRegistrationDTO.getPassword()));

        user.setSignUpDate(Calendar.getInstance());
        user.setUserPublicKey(UUID.randomUUID());

        Role role = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new NoSuchElementException("Permissão não achada"));

        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        Set<GrantedAuthority> roles = user.getRoles()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(
                user.getUserPublicKey().toString(),
                user.getPassword(),
                roles
        );
    }

    @Override
    public Authentication login(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken((
                        email),
                        password));
    }

    public Authentication generateAuthentication(Jwt jwt) {

        User user = userRepository.findByUserPublicKey(UUID.fromString(jwt.getClaim("sub")))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao existe"));

        Set<GrantedAuthority> roles = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                roles
        );
    }

    public Optional<User> validateEmail(String email) {
        return userRepository.findByEmail(email);
    }
}