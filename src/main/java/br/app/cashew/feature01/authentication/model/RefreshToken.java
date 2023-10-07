package br.app.cashew.feature01.authentication.model;

import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshTokenID")
    private int refreshTokenID;

    @Column(unique = true)
    private UUID jti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userID",
            referencedColumnName = "userID"
    )
    private User user;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(
            name = "partnerID",
            referencedColumnName = "partnerID"
    )
    private Partner partner;
}
