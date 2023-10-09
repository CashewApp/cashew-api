package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer> {

    List<Campus> findByUniversity(University university);

    Optional<Campus> findByPublicKey(UUID publicKey);

    boolean existsByNameAndUniversity(String name, University university);

}
