package br.app.cashew.feature01.authentication.model.partner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "owner")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ownerID;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "owner_partner_id")
    private Partner partner;

    // metodos add e remove para propagacao de estado de universidade e deixar a camada de persistencia e BD sincronizados
}
