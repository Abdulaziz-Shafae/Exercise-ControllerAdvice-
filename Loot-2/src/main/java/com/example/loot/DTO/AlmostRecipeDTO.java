package com.example.loot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlmostRecipeDTO {

    private String name;

    private String description;

    private String instructions;

    private String category;

    private List<String> ingredients;

    private List<MissingIngredientDTO> missing;
}