package br.app.cashew.feature01.authentication.repository;

import br.app.cashew.feature01.authentication.model.partner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
