package br.app.cashew.feature03.cafeteria.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "campus")
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campusID;

    @Size(max = 255)
    @NotEmpty
    private String name;

    private UUID publicKey;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "universityID", referencedColumnName = "universityID", nullable = false)
    private University university;

    @JsonManagedReference
    @OneToMany(mappedBy = "campus")
    private List<Cafeteria> cafeterias;
}
// TODO Criar robo que ira fazer web scraping do e mec do SISU para confirmar dados de campus e universidade passadas