package br.app.cashew.feature03.cafeteria.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "productID")
    private int productID;

    @NotEmpty
    @Size(max = 40)
    private String name; 

    @Size(max = 255)
    private String description;

    // private foto foto;

    @NotNull
    @Column(nullable = false, name = "value")
    private double value;

    @NotNull
    @Column(name = "status", columnDefinition = "tinyint(1) default 0", nullable = false)
    private boolean status;


    @ManyToOne(cascade =  CascadeType.ALL, optional = false)
    @JoinColumn(name = "cafeteriaID", nullable = false)
    private Cafeteria cafeteria;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(
            name = "stockID",
            referencedColumnName = "stockID",
            unique = true,
            updatable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
    private Category category;


    @ManyToMany()
    @JoinTable(
            name = "product_ingredient",
            joinColumns = @JoinColumn(name = "productID", referencedColumnName = "productID"),
            inverseJoinColumns = @JoinColumn(name = "ingredientID", referencedColumnName = "ingredientID")
    )
    private List<Ingredient> ingredients;
}
// TODO no futuro modificar a entidade produto de maneira a permitir a emissão de promoções
