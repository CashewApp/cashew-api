package br.app.cashew.feature01.authentication.repository;

import br.app.cashew.feature01.authentication.model.partner.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {

    boolean existsByEmail(String email);

    Optional<Partner> findByEmail(String email);

    Optional<Partner> findByPartnerPublicKey(UUID partnerPublicKey);
}
