package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.Ingredient;
import com.example.loot.Model.PantryItem;
import com.example.loot.Model.User;
import com.example.loot.Repository.IngredientRepository;
import com.example.loot.Repository.PantryItemRepository;
import com.example.loot.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 pantry item not found
//1 success
//2 user not found
//3 ingredient not found
//4 ingredient already exists in user's pantry

public class PantryItemService {

    private final PantryItemRepository pantryItemRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;


    public List<PantryItem> getPantryItems(){
        return pantryItemRepository.findAll();
    }


    public void addPantryItem(PantryItem pantryItem){

        User checkUser = userRepository.findUserById(pantryItem.getUserId());

        //user not found
        if(checkUser == null){
            throw new ApiException("User not found");
        }

        Ingredient checkIngredient = ingredientRepository.findIngredientById(pantryItem.getIngredientId());

        //ingredient not found
        if(checkIngredient == null){
            throw new ApiException("Ingredient not found");
        }

        PantryItem checkPantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(pantryItem.getUserId(), pantryItem.getIngredientId());

        //ingredient already exists in user's pantry
        if(checkPantryItem != null){
            throw new ApiException("Ingredient already exists in user's pantry");
        }

        //added
        pantryItemRepository.save(pantryItem);
    }


    public void editPantryItem(Integer id, PantryItem pantryItem){

        PantryItem oldPantryItem = pantryItemRepository.findPantryItemById(id);

        //pantry item not found
        if(oldPantryItem == null){
            throw new ApiException("Pantry item not found");
        }

        oldPantryItem.setQuantity(pantryItem.getQuantity());
        oldPantryItem.setLowStockThreshold(pantryItem.getLowStockThreshold());

        //updated
        pantryItemRepository.save(oldPantryItem);
    }


    public void deletePantryItem(int id){

        PantryItem oldPantryItem = pantryItemRepository.findPantryItemById(id);

        //pantry item not found
        if(oldPantryItem == null){
            throw new ApiException("Pantry item not found");
        }

        //deleted
        pantryItemRepository.delete(oldPantryItem);
    }
}