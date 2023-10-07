package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUser;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUserKey;
import br.app.cashew.feature01.authentication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityUserRepository extends JpaRepository<UniversityUser, UniversityUserKey> {

    List<UniversityUser> findByUniversityUserIDUserID(User user);
}
