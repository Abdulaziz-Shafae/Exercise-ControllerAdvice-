package com.example.loot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeneratedRecipeDTO {

    private String name;

    private String description;

    private String category;

    private String instructions;

    private List<GeneratedRecipeIngredientDTO> ingredients;
}