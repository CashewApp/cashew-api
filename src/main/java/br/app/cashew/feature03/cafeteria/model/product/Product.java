package br.app.cashew.feature03.cafeteria.model.product;


import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.Category;
import br.app.cashew.feature03.cafeteria.model.Ingredient;
import br.app.cashew.feature03.cafeteria.model.Stock;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

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

    private String photoUrl;

    @NotNull
    @Column(nullable = false, name = "price")
    private double price;

    @NotNull
    @Column(name = "status", columnDefinition = "tinyint(1) default 0", nullable = false)
    private boolean status;

    private UUID publicKey;

    @ManyToOne(cascade =  CascadeType.ALL, optional = false)
    @JoinColumn(name = "cafeteriaID", nullable = false)
    private Cafeteria cafeteria;

    @JsonManagedReference
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(
            name = "stockID",
            referencedColumnName = "stockID",
            unique = true,
            updatable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "product_ingredient",
            joinColumns = @JoinColumn(name = "productID", referencedColumnName = "productID"),
            inverseJoinColumns = @JoinColumn(name = "ingredientID", referencedColumnName = "ingredientID")
    )
    private List<Ingredient> ingredients;
}
// TODO no futuro modificar a "produto" de maneira a permitir a emissão de promoções
// TODO no futuro modificar "produto" de maneira a permitir que um produto tenha varios tipos/sabores/opcoes
