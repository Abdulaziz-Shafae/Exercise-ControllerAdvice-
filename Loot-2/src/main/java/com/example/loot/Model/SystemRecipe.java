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
public class SystemRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank( message = "The name cant be blank")
    @NotEmpty( message = "The name cant be empty")
    @Size(max = 150, message = "The name must not exceed 150 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "The name must contain only letters and spaces"
    )
    @Column(columnDefinition = "VARCHAR(150) not null unique")
    private String name;

    @Size(max = 500, message = "The description must not exceed 500 characters")
    @Column(columnDefinition = "VARCHAR(500)")
    private String description;

    @NotBlank(message = "The instructions cant be blank")
    @NotEmpty(message = "The instructions cant be empty")
    @Column(columnDefinition = "TEXT not null")
    private String instructions;

    @NotBlank(message = "The category cant be blank")
    @NotEmpty(message = "The category cant be empty")
    @Pattern(
            regexp = "^(Breakfast|Lunch|Dinner|Snack)$",
            message = "The category must be Breakfast, Lunch, Dinner, or Snack"
    )
    @Check(constraints = "category = 'Breakfast' OR category = 'Lunch' OR category = 'Dinner' OR category = 'Snack'")
    @Column(columnDefinition = "VARCHAR(9) not null")
    private String category;
}
