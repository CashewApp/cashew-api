package br.app.cashew.feature03.cafeteria.model;

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
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "stockID")
    private int stockID;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdate", nullable = false)
    private Calendar lastUpdate;

    @NotNull
    @Column(name = "availableQuantity", nullable = false)
    private int availableQuantity;

    @NotNull
    @Column(name = "orderedQuantity", nullable = false)
    private int orderedQuantity;
}

