package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CookingHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "The user ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "The recipe ID cant be empty")
    @Column(columnDefinition = "int not null")
    private Integer recipeId;


    @NotBlank(message = "The recipe name cant be blank")
    @NotEmpty(message = "The recipe name cant be empty")
    @Size(max = 150, message = "The recipe name must not exceed 150 characters")
    @Column(columnDefinition = "VARCHAR(150) not null")
    private String recipeName;

    @NotBlank(message = "The description cant be blank")
    @NotEmpty(message = "The description cant be empty")
    @Size(max = 500, message = "The description must not exceed 500 characters")
    @Column(columnDefinition = "VARCHAR(500) not null")
    private String description;

    @NotBlank(message = "The instructions cant be blank")
    @NotEmpty(message = "The instructions cant be empty")
    @Size(max = 1000, message = "The instructions must not exceed 1000 characters")
    @Column(columnDefinition = "VARCHAR(1000) not null")
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

    @NotBlank(message = "The recipe type cant be blank")
    @NotEmpty(message = "The recipe type cant be empty")
    @Pattern(
            regexp = "^(System|User)$",
            message = "The recipe type must be System or User"
    )
    @Check(constraints = "recipe_type = 'System' OR recipe_type = 'User'")
    @Column(columnDefinition = "VARCHAR(6) not null")
    private String recipeType;


    @Column(columnDefinition = "DATETIME not null")
    private LocalDateTime cookedAt;

}
