package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.CookingHisIng;
import com.example.loot.Model.CookingHistory;
import com.example.loot.Model.Ingredient;
import com.example.loot.Repository.CookingHisIngRepository;
import com.example.loot.Repository.CookingHistoryRepository;
import com.example.loot.Repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 cooking history ingredient not found
//1 success
//2 cooking history not found
//3 ingredient not found
//4 ingredient already exists in cooking history

public class CookingHisIngService {

    private final CookingHisIngRepository cookingHisIngRepository;
    private final CookingHistoryRepository cookingHistoryRepository;
    private final IngredientRepository ingredientRepository;


    public List<CookingHisIng> getCookingHisIng(){
        return cookingHisIngRepository.findAll();
    }


    public void addCookingHisIng(CookingHisIng cookingHisIng) {

        CookingHistory checkCookingHistory = cookingHistoryRepository.findCookingHistoryById(cookingHisIng.getCookingHistoryId());

        // cooking history not found
        if (checkCookingHistory == null) {
            throw new ApiException("Cooking history not found");
        }

        Ingredient checkIngredient = ingredientRepository.findIngredientById(cookingHisIng.getIngredientId());

        // ingredient not found
        if (checkIngredient == null) {
            throw new ApiException("Ingredient not found");
        }

        CookingHisIng checkCookingHisIng = cookingHisIngRepository.findCookingHisIngByCookingHistoryIdAndIngredientId(cookingHisIng.getCookingHistoryId(), cookingHisIng.getIngredientId());

        // ingredient already exists in cooking history
        if (checkCookingHisIng != null) {
            throw new ApiException("Ingredient already exists in cooking history");
        }

        // Take snapshot from Ingredient
        cookingHisIng.setIngredientName(checkIngredient.getName());
        cookingHisIng.setUnit(checkIngredient.getUnit());

        // added
        cookingHisIngRepository.save(cookingHisIng);
    }

    public void editCookingHisIng(Integer id, CookingHisIng cookingHisIng){

        CookingHisIng oldCookingHisIng = cookingHisIngRepository.findCookingHisIngById(id);

        //cooking history ingredient not found
        if(oldCookingHisIng == null){
            throw new ApiException("Cooking history ingredient not found");
        }

        oldCookingHisIng.setUsedQuantity(cookingHisIng.getUsedQuantity());

        //updated
        cookingHisIngRepository.save(oldCookingHisIng);
    }


    public void deleteCookingHisIng(int id){

        CookingHisIng oldCookingHisIng = cookingHisIngRepository.findCookingHisIngById(id);

        //cooking history ingredient not found
        if(oldCookingHisIng == null){
            throw new ApiException("Cooking history ingredient not found");
        }

        //deleted
        cookingHisIngRepository.delete(oldCookingHisIng);
    }
}