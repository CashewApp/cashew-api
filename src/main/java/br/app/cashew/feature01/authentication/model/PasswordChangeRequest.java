package br.app.cashew.feature01.authentication.model;

import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@Entity
@Table(name = "password_change_request")
@NoArgsConstructor
public class PasswordChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_change_request_id")
    private int passwordChangeRequestsID;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time;

    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "userID",
            referencedColumnName = "userID",
            nullable = false
    )
    private User user;
}
