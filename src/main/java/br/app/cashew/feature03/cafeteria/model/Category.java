package br.app.cashew.feature03.cafeteria.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Category")
@Getter
@Setter
public class Category {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "categoryID")
    private int categoryID;
    
    @NotEmpty
    @Size(max = 40)
    @Column(name = "name", nullable = false)
    private String name; 
}
