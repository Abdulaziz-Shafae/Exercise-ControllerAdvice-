package com.example.loot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientSubstituteDTO {

    private String missingIngredient;

    private String substitute;

    private Double quantity;

    private String unit;

    private String reason;
}