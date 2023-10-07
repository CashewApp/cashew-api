package br.app.cashew.feature03.cafeteria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ingredient")
@NoArgsConstructor
public class Ingredient {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ingredientID")
        private int ingredientID;

        @Column(nullable = false)
        private String name;

        @ManyToOne(cascade = CascadeType.ALL, optional = false)
        @JoinColumn(
                name = "cafeteriaID",
                referencedColumnName = "cafeteriaID"
        )
        private Cafeteria cafeteria;

}