package com.example.loot.Repository;

import com.example.loot.Model.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecipeRepository extends JpaRepository<UserRecipe, Integer> {
    UserRecipe findUserRecipeById(Integer id);
    UserRecipe findUserRecipeByIdAndUserId(Integer id, Integer userId);
    UserRecipe findUserRecipeByUserIdAndName(Integer userId, String name);

    List<UserRecipe> findUserRecipeByUserIdAndCategory(Integer userId, String category);

    List<UserRecipe> findUserRecipeByUserId(Integer userId);
}
