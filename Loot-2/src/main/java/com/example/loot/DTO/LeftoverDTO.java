package com.example.loot.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeftoverDTO {

    @NotBlank(message = "Ingredient name cannot be empty")
    private String name;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than 0")
    private Double quantity;

    @NotBlank(message = "Unit cannot be empty")
    @Pattern(
            regexp = "^(g|ml|piece)$",
            message = "The unit must be g, ml, or piece"
    )
    private String unit;
}