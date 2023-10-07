package br.app.cashew.feature01.authentication.repository;

import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    boolean existsByJti(UUID jti);
    void deleteAllByUser(User user);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM refresh_token WHERE jti = :jti", nativeQuery = true)
    void removeByJti(@Param("jti") UUID jti);

    boolean existsRefreshTokenByJtiAndUser(UUID jti, User user);

    boolean existsRefreshTokenByJtiAndPartner(UUID jti, Partner partner);
}
