package br.app.cashew.feature02.user.model;

import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Setter
@Getter
@Entity
@Table(name = "email_change_request")
public class EmailChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_change_request_id")
    private int emailChangeRequestID;

    private String pin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Calendar time;

    private String email;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", referencedColumnName = "userID", updatable = false)
    private User user;
}
// TODO fix: validacao do refresh token, o mesmo continua valido quando ja utilizado uma vez e nao presente no banco de dados