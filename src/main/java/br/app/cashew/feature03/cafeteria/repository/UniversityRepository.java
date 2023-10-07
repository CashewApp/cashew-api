package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    Optional<University> findByNameAndAcronym(String name, String acronym);

    List<University> findByNameStartsWithOrAcronymStartsWith(String name, String acronym);

    Optional<University> findByUniversityPublicKey(UUID universityPublicKey);

    boolean existsByNameAndAcronym(String name, String acronym);

}
