package br.app.cashew.feature02.user.repository;

import br.app.cashew.feature02.user.model.EmailChangeRequest;
import br.app.cashew.feature01.authentication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, Integer> {

    Optional<EmailChangeRequest> findByPinAndUser(String pin, User user);
    boolean existsByPinAndUser(String pin, User user);
}
