package br.app.cashew.feature03.cafeteria.model.universityuser;

import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "university_user_preferences")
public class UniversityUser {

    @EmbeddedId
    private UniversityUserKey universityUserID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "universityID")
    @MapsId("universityID")
    private University university;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userID")
    @MapsId("userID")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campusID", referencedColumnName = "campusID", nullable = false, updatable = false)
    private Campus campus;
}
