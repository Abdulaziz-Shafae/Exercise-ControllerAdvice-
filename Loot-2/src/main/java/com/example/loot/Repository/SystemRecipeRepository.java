package com.example.loot.Repository;

import com.example.loot.Model.SystemRecipe;
import com.example.loot.Model.UserRecIng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemRecipeRepository extends JpaRepository <SystemRecipe, Integer> {
    SystemRecipe findSystemRecipeById(Integer id);
    SystemRecipe findSystemRecipeByName(String name);
    List<SystemRecipe> findSystemRecipeByCategory(String category);
}
