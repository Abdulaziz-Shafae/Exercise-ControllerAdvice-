package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.Ingredient;
import com.example.loot.Model.UserRecIng;
import com.example.loot.Model.UserRecipe;
import com.example.loot.Repository.IngredientRepository;
import com.example.loot.Repository.UserRecIngRepository;
import com.example.loot.Repository.UserRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 user recipe ingredient not found
//1 success
//2 user recipe not found
//3 ingredient not found
//4 ingredient already exists in user recipe

public class UserRecIngService {

    private final UserRecIngRepository userRecIngRepository;
    private final UserRecipeRepository userRecipeRepository;
    private final IngredientRepository ingredientRepository;


    public List<UserRecIng> getUserRecIng(){
        return userRecIngRepository.findAll();
    }


    public void addUserRecIng(UserRecIng userRecIng){

        UserRecipe checkUserRecipe = userRecipeRepository.findUserRecipeById(userRecIng.getUserRecipeId());

        //user recipe not found
        if(checkUserRecipe == null){
            throw new ApiException("User recipe not found");
        }

        Ingredient checkIngredient = ingredientRepository.findIngredientById(userRecIng.getIngredientId());

        //ingredient not found
        if(checkIngredient == null){
            throw new ApiException("Ingredient not found");
        }

        UserRecIng checkUserRecIng = userRecIngRepository.findUserRecIngByUserRecipeIdAndIngredientId(userRecIng.getUserRecipeId(), userRecIng.getIngredientId());

        //ingredient already exists in user recipe
        if(checkUserRecIng != null){
            throw new ApiException("Ingredient already exists in user recipe");
        }

        //added
        userRecIngRepository.save(userRecIng);
    }


    public void editUserRecIng(Integer id, UserRecIng userRecIng){

        UserRecIng oldUserRecIng = userRecIngRepository.findUserRecIngById(id);

        //user recipe ingredient not found
        if(oldUserRecIng == null){
            throw new ApiException("User recipe ingredient not found");
        }

        oldUserRecIng.setRequiredQuantity(userRecIng.getRequiredQuantity());

        //updated
        userRecIngRepository.save(oldUserRecIng);
    }


    public void deleteUserRecIng(int id){

        UserRecIng oldUserRecIng = userRecIngRepository.findUserRecIngById(id);

        //user recipe ingredient not found
        if(oldUserRecIng == null){
            throw new ApiException("User recipe ingredient not found");
        }

        //deleted
        userRecIngRepository.delete(oldUserRecIng);
    }
}