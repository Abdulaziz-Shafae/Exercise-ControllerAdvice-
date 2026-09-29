package com.example.loot.Repository;

import com.example.loot.Model.CookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookingHistoryRepository extends JpaRepository <CookingHistory , Integer> {
    CookingHistory findCookingHistoryById(Integer id);
    List<CookingHistory> findCookingHistoryByUserId(Integer userId);

    CookingHistory findCookingHistoryByIdAndUserId(Integer historyId, Integer userId);
    List<CookingHistory> findCookingHistoryByUserIdAndCategoryIgnoreCase(Integer userId, String category);
}
