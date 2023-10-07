package br.app.cashew.feature01.authentication.repository;

import br.app.cashew.feature01.authentication.model.PasswordChangeRequest;
import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordChangeRequestRepository extends JpaRepository<PasswordChangeRequest, Integer> {
    Optional<PasswordChangeRequest> findByToken(String token);

    Optional<PasswordChangeRequest> findFirstByUserOrderByTimeDesc(User user);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE PasswordChangeRequest p SET p.token = null WHERE p.token = :token")
    void updateTokenByToken(@Param("token") String token);

    @Query("SELECT p.user FROM PasswordChangeRequest p WHERE p.token = :passwordChangeRequestToken")
    Optional<User> findUserByToken(@Param("passwordChangeRequestToken") String passwordChangeRequestToken);
}
