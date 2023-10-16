package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer> {

    List<Campus> findByUniversity(University university);

    Optional<Campus> findByPublicKey(UUID publicKey);

    boolean existsByNameAndUniversity(String name, University university);

    @Query("SELECT c.cafeterias FROM Campus c WHERE c.publicKey =:campusPublicKey ")
    List<Cafeteria> findCafeteriasByPublicKey(@Param("campusPublicKey") UUID publicKey);

}
