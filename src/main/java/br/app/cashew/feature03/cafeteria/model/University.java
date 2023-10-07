package br.app.cashew.feature03.cafeteria.model;

import br.app.cashew.global.hibernate.constraints.uf.ValidUF;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "university")
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int universityID;

    @NotEmpty(message = "Nome invalido")
    @Size(max = 255, message = "Nome invalido. Numero maximo de 40 caracteres")
    private String name;

    @NotEmpty(message = "Sigla invalida")
    private String acronym;

    @Column(name = "university_public_key")
    private UUID universityPublicKey;

    @ValidUF
    @NotEmpty(message = "estado invalido")
    private String state;

    @JsonIgnore
    @OneToMany(mappedBy = "university", orphanRemoval = true)
    private List<Campus> campuses;
}
