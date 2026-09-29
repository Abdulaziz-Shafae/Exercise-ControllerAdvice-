package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "ingredient_id"})})
public class PantryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull( message = "The user ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull( message = "The ingredient ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer ingredientId;

    @NotNull( message = "The quantity cant be empty")
    @Min(value = 0 , message = "The quantity must be 0 or more")
    @Check(constraints = "quantity >= 0")
    @Column(columnDefinition = "double not null")
    private Double quantity;

    @NotNull( message = "The low stock threshold cant be empty")
    @Min(value = 0 , message = "The low stock threshold must be 0 or more")
    @Check(constraints = "low_stock_threshold >= 0")
    @Column(columnDefinition = "double not null")
    private Double lowStockThreshold;
}
