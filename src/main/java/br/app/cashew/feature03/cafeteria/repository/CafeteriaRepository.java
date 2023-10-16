package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CafeteriaRepository extends JpaRepository<Cafeteria, Integer> {

    boolean existsByCnpj(String cnpj);

    Optional<Cafeteria> findByPublicKey(UUID publicKey);
}
