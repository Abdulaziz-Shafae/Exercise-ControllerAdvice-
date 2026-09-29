package com.example.loot.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeGeneratorRequestDTO {

    @NotBlank(message = "Request cannot be empty")
    private String request;
}
