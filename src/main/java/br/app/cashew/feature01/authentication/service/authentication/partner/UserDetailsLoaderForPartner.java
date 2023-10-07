package br.app.cashew.feature01.authentication.service.authentication.partner;

import br.app.cashew.feature01.authentication.exception.email.EmailNotFoundException;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature01.authentication.service.authentication.UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsLoaderForPartner implements UserDetailsLoader {

    private final PartnerRepository partnerRepository;

    @Autowired
    public UserDetailsLoaderForPartner(PartnerRepository partnerRepository) {

        this.partnerRepository = partnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Partner partner = partnerRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EmailNotFoundException("Não foi possível encontrar sua conta do Cashew", "email"));

        Set<GrantedAuthority> roles = partner
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                partner.getPartnerPublicKey().toString(),
                partner.getPassword(),
                roles
        );
    }
}
