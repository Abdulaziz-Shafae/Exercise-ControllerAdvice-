package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CookingHisIng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "The cooking history ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer cookingHistoryId;


    @NotNull(message = "The ingredient ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer ingredientId;

    @NotBlank(message = "The ingredient name cant be blank")
    @NotEmpty(message = "The ingredient name cant be empty")
    @Size(max = 100, message = "The ingredient name must not exceed 100 characters")
    @Column(columnDefinition = "VARCHAR(100) not null")
    private String ingredientName;

    @NotNull(message = "The used quantity cant be empty")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "The used quantity must be more than 0"
    )
    @Check(constraints = "used_quantity > 0")
    @Column(columnDefinition = "double not null")
    private Double usedQuantity;

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
