package br.app.cashew.feature01.authentication.model.user;

import br.app.cashew.feature01.authentication.model.PasswordChangeRequest;
import br.app.cashew.feature01.authentication.model.RefreshToken;
import br.app.cashew.feature01.authentication.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    public User( String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "userID")
    private int userID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    // configurar geração automatica
    @Column(nullable = false, unique = true, name = "userPublicKey")
    private UUID userPublicKey;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "signUpDate", nullable = false)
    private Calendar signUpDate;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "removeDate")
    private Calendar removeDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userID", referencedColumnName = "userID"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID")
    )
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RefreshToken> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PasswordChangeRequest> passwordChangeRequests;
}
