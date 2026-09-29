package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.Ingredient;
import com.example.loot.Repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 ingredient not found
//1 success
//2 ingredient name already taken

public class IngredientService {

    private final IngredientRepository ingredientRepository;


    public List<Ingredient> getIngredients(){
        return ingredientRepository.findAll();
    }


    public void addIngredient(Ingredient ingredient){

        Ingredient oldIngredient = ingredientRepository.findIngredientByName(ingredient.getName());

        //name taken
        if(oldIngredient != null){
            throw new ApiException("Ingredient name already taken");
        }

        //added
        ingredientRepository.save(ingredient);
    }


    public void editIngredient(Integer id, Ingredient ingredient){

        Ingredient oldIngredient = ingredientRepository.findIngredientById(id);

        //ingredient not found
        if(oldIngredient == null){
            throw new ApiException("Ingredient not found");
        }

        Ingredient nameCheck = ingredientRepository.findIngredientByName(ingredient.getName());

        //name taken by another ingredient
        if(nameCheck != null && !nameCheck.getId().equals(id)){
            throw new ApiException("Ingredient name already taken");
        }

        oldIngredient.setName(ingredient.getName());
        oldIngredient.setUnit(ingredient.getUnit());

        //updated
        ingredientRepository.save(oldIngredient);
    }


    public void deleteIngredient(int id){

        Ingredient oldIngredient = ingredientRepository.findIngredientById(id);

        //ingredient not found
        if(oldIngredient == null){
            throw new ApiException("Ingredient not found");
        }

        //deleted
        ingredientRepository.delete(oldIngredient);
    }
}