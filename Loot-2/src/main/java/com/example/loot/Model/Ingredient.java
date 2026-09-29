package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank( message = "The name cant be blank")
    @NotEmpty( message = "The name cant be empty")
    @Size(max = 100, message = "The name must not exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "The name must contain only letters and spaces"
    )
    @Column(columnDefinition = "VARCHAR(100) not null unique")
    private String name;

    @NotBlank( message = "The unit cant be blank")
    @NotEmpty( message = "The unit cant be empty")
    @Size(max = 5, message = "The unit must not exceed 5 characters")
    @Pattern(
            regexp = "^(g|ml|piece)$",
            message = "The unit must be g, ml, or piece"
    )
    @Check(constraints = "unit = 'g' OR unit = 'ml' OR unit = 'piece'")
    @Column(columnDefinition = "VARCHAR(5) not null")
    private String unit;
}
