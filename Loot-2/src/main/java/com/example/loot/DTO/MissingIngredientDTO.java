package com.example.loot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissingIngredientDTO {

    private String name;

    // Quantity required by the recipe
    private Double required;

    // Quantity currently available in user's pantry
    private Double available;

    // Quantity the user still needs
    private Double missing;
}