package br.app.cashew.feature01.authentication.model.partner;

import br.app.cashew.feature01.authentication.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "partner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partnerID;

    @Size(max = 40 , message = "Nome invalido. Maximo de 40 caracteres")
    @NotEmpty(message = "Nome invalido")
    private String name;

    @NotNull(message = "Email invalido")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Senha invalida")
    @Size(min = 8, message = "Senha invalida. Minimo de 8 digitos")
    private String password;

    private String cpf;

    private UUID partnerPublicKey;

    // dados bancarios, fazer no futuro - tem que ver como a API do mercado pago funciona

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar signUpDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "partner_role",
            joinColumns = @JoinColumn(name = "partnerID", referencedColumnName = "partnerID"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Owner owner;

    public void setOwner(Owner owner) {

        if (owner == null) {
            if (this.owner != null) {
                this.owner.setPartner(null);
            }
        }
        else {
            owner.setPartner(this);
        }

        this.owner = owner;
    }
}
