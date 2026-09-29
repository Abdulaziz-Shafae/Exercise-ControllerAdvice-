package com.example.loot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LowStockDTO {

    private String name;

    private String available;

    private String threshold;

    private String needToBuy;
}