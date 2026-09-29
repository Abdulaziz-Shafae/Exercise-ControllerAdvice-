package com.example.loot.Repository;

import com.example.loot.Model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository <Ingredient, Integer> {
    Ingredient findIngredientById(Integer id);

    Ingredient findIngredientByName(String name);

    Ingredient findIngredientByNameIgnoreCase(String name);
}
