package br.app.cashew.feature01.authentication.repository;

import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);

    Optional<User> findByUserPublicKey(UUID uuid);

    @Query("SELECT u.password FROM User u WHERE u.userPublicKey = :userPublicKey")
    String findPasswordByUserPublicKey(@Param("userPublicKey") UUID userPublicKey);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userPublicKey = :userPublicKey")
    void updatePasswordByUserPublicKey(@Param("userPublicKey") UUID userPublicKey, @Param("newPassword") String newPassword);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.cpf = :cpf WHERE u.email = :email")
    void updateCpfByEmail(@Param("email") String email, @Param("cpf") String cpf);

    boolean existsByCpf(String cpf);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.name = :name WHERE u.email = :email")
    void updateNameByEmail(@Param("email") String email, @Param("name") String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE User u SET u.email = :email WHERE u.userPublicKey = :userPublicKey")
    void updateEmailByUserPublicKey(@Param("userPublicKey") UUID userPublicKey, @Param("email") String email);
}