package br.app.cashew.feature03.cafeteria.model;

import br.app.cashew.feature03.cafeteria.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "stockID")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "stockID")
    private int stockID;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Calendar lastUpdate;

    @NotNull
    @Column(name = "availableQuantity", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int availableQuantity;

    @NotNull
    @Column(name = "orderedQuantity", nullable = false, columnDefinition = "int DEFAULT 0")
    private int orderedQuantity;

    @JsonBackReference
    @OneToOne(mappedBy = "stock")
    private Product product;
}