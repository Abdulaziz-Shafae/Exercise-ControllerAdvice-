package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_recipe_id", "ingredient_id"})})
public class UserRecIng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull( message = "The user recipe ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer userRecipeId;


    @NotNull( message = "The ingredient ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer ingredientId;


    @NotNull( message = "The required quantity cant be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "The required quantity must be more than 0")
    @Check(constraints = "required_quantity > 0")
    @Column(columnDefinition = "double not null")
    private Double requiredQuantity;
}
