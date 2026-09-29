package com.example.loot.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageToIngredientDTO {

    private String name;

    private Double quantity;

    @Pattern(
            regexp = "^(g|ml|piece)?$",
            message = "The unit must be g, ml, or piece"
    )
    private String unit;
}
