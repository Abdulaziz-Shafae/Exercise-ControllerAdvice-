package com.example.loot.Repository;

import com.example.loot.Model.PantryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PantryItemRepository extends JpaRepository<PantryItem, Integer> {
    PantryItem findPantryItemById(Integer id);
    PantryItem findPantryItemByUserIdAndIngredientId(Integer userId, Integer ingredientId);

    @Query("SELECT p FROM PantryItem p WHERE p.userId = :userId AND p.quantity <= p.lowStockThreshold")
    List<PantryItem> findLowStockByUserId(Integer userId);

}
