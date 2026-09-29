package com.example.loot.Repository;

import com.example.loot.Model.UserRecIng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecIngRepository extends JpaRepository<UserRecIng, Integer> {
    UserRecIng findUserRecIngById(Integer id);
    UserRecIng findUserRecIngByUserRecipeIdAndIngredientId(Integer userRecipeId, Integer ingredientId);
    List<UserRecIng> findUserRecIngByUserRecipeId(Integer userRecipeId);
}
