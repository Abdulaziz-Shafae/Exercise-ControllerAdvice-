package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.Ingredient;
import com.example.loot.Model.SystemRecIng;
import com.example.loot.Model.SystemRecipe;
import com.example.loot.Repository.IngredientRepository;
import com.example.loot.Repository.SystemRecIngRepository;
import com.example.loot.Repository.SystemRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 system recipe ingredient not found
//1 success
//2 system recipe not found
//3 ingredient not found
//4 ingredient already exists in system recipe

public class SystemRecIngService {

    private final SystemRecIngRepository systemRecIngRepository;
    private final SystemRecipeRepository systemRecipeRepository;
    private final IngredientRepository ingredientRepository;


    public List<SystemRecIng> getSystemRecIngs(){
        return systemRecIngRepository.findAll();
    }


    public void addSystemRecIng(SystemRecIng systemRecIng){

        SystemRecipe checkSystemRecipe = systemRecipeRepository.findSystemRecipeById(systemRecIng.getSystemRecipeId());

        //system recipe not found
        if(checkSystemRecipe == null){
            throw new ApiException("System recipe not found");
        }

        Ingredient checkIngredient = ingredientRepository.findIngredientById(systemRecIng.getIngredientId());

        //ingredient not found
        if(checkIngredient == null){
            throw new ApiException("Ingredient not found");
        }

        SystemRecIng checkSystemRecIng = systemRecIngRepository.findSystemRecIngBySystemRecipeIdAndIngredientId(systemRecIng.getSystemRecipeId(), systemRecIng.getIngredientId());

        //ingredient already exists in system recipe
        if(checkSystemRecIng != null){
            throw new ApiException("Ingredient already exists in system recipe");
        }

        //added
        systemRecIngRepository.save(systemRecIng);
    }


    public void editSystemRecIng(Integer id, SystemRecIng systemRecIng){

        SystemRecIng oldSystemRecIng = systemRecIngRepository.findSystemRecIngById(id);

        //system recipe ingredient not found
        if(oldSystemRecIng == null){
            throw new ApiException("System recipe ingredient not found");
        }

        oldSystemRecIng.setRequiredQuantity(systemRecIng.getRequiredQuantity());

        //updated
        systemRecIngRepository.save(oldSystemRecIng);
    }


    public void deleteSystemRecIng(int id){

        SystemRecIng oldSystemRecIng = systemRecIngRepository.findSystemRecIngById(id);

        //system recipe ingredient not found
        if(oldSystemRecIng == null){
            throw new ApiException("System recipe ingredient not found");
        }

        //deleted
        systemRecIngRepository.delete(oldSystemRecIng);
    }
}