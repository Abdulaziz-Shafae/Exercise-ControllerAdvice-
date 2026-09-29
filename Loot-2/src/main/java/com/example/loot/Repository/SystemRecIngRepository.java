package com.example.loot.Repository;

import com.example.loot.Model.SystemRecIng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemRecIngRepository extends JpaRepository <SystemRecIng, Integer> {
    SystemRecIng findSystemRecIngById(Integer id);
    SystemRecIng findSystemRecIngBySystemRecipeIdAndIngredientId(Integer systemRecipeId, Integer ingredientId);
    List<SystemRecIng> findSystemRecIngBySystemRecipeId(Integer systemRecipeId);

}
