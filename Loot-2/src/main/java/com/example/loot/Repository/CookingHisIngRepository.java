package com.example.loot.Repository;

import com.example.loot.Model.CookingHisIng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookingHisIngRepository extends JpaRepository <CookingHisIng, Integer> {
    CookingHisIng findCookingHisIngById(Integer id);
    CookingHisIng findCookingHisIngByCookingHistoryIdAndIngredientId(Integer cookingHistoryId, Integer ingredientId);
    List<CookingHisIng> findCookingHisIngByCookingHistoryId(Integer cookingHistoryId);
}
