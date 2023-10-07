package br.app.cashew.feature03.cafeteria.model;

import br.app.cashew.feature01.authentication.model.partner.Owner;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "cafeteria")
@Getter
@Setter
@NoArgsConstructor
public class Cafeteria{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafeteriaID")
    private long cafeteriaID;

    @NotBlank(message = "Nome invalido")
    @Column(name = "name", nullable = false)
    @Size(max = 255 , message = "Nome invalido. Maximo de 255 caracteres")
    private String  name;

    @Column(name = "phone")
    private String phone;

    @NotBlank
    @Column(name = "description")
    @Size(max = 255, message = "Descricao excede os limite de caracteres")
    private String description;

    @Column(nullable = false, unique = true, name = "public_key")
    private UUID publicKey;

    @Column(name = "cnpj", unique = true, nullable = false)
    @Size(min = 14, max = 14, message = "CNPJ invalido")
    private String cnpj;

    // TODO implementar upload de fotos utilizando o S3
    // private String access_token;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "ownerID",
            referencedColumnName = "owner_partner_id"
    )
    private Owner owner;

    @JsonBackReference
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "campusID", referencedColumnName = "campusID", nullable = false)
    private Campus campus;
}