package br.app.cashew.feature01.authentication.service.authentication.partner;

import br.app.cashew.feature01.authentication.exception.email.EmailAlreadyExistsException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.Role;
import br.app.cashew.feature01.authentication.model.partner.Owner;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature01.authentication.repository.RoleRepository;
import br.app.cashew.feature01.authentication.service.authentication.BaseAuthenticationService;
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

@Service("partnerAuthenticationServiceImpl")
public class PartnerAuthenticationServiceImpl implements BaseAuthenticationService<Partner> {

    private final PartnerRepository partnerRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public PartnerAuthenticationServiceImpl(
            PartnerRepository partnerRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {

        this.partnerRepository = partnerRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication register(Partner partner) {

        boolean partnerExists = partnerRepository.existsByEmail(partner.getEmail());

        if (partnerExists) {
            throw new EmailAlreadyExistsException("Este e-mail ja esta em uso. Tente novamente", "email");
        }

        // TODO refatorar para PartnerAuthenticationServiceImpl validar o CPF com "UserAccountService" durante o cadastro

        partner.setSignUpDate(Calendar.getInstance());
        partner.setPartnerPublicKey(UUID.randomUUID());
        partner.setPassword(passwordEncoder.encode(partner.getPassword()));

        Role role = roleRepository.findByAuthority("OWNER")
                .orElseThrow(() -> new NoSuchElementException("Permissão não achada"));

        partner.setRoles(Collections.singleton(role));

        Owner owner = new Owner();
        partner.setOwner(owner);

        partnerRepository.save(partner);

        Set<GrantedAuthority> roles = partner.getRoles()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(
                partner.getPartnerPublicKey().toString(),
                partner.getPassword(),
                roles
        );
    }

    @Override
    public Authentication login(String email, String password) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
    }

    @Override
    public Authentication generateAuthentication(Jwt jwt) {
        Partner partner = partnerRepository.findByPartnerPublicKey(UUID.fromString(jwt.getClaim("sub")))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao existe"));

        Set<GrantedAuthority> roles = partner
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                roles
        );
    }

    @Override
    public Optional<User> validateEmail(String email) {
        return Optional.empty();
    }
}

