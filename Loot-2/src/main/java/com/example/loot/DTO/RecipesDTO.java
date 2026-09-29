package com.example.loot.DTO;

import com.example.loot.Model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipesDTO {

    private String name;

    private String description;

    private String instructions;

    private String category;

    private List<String> ingredients;
}