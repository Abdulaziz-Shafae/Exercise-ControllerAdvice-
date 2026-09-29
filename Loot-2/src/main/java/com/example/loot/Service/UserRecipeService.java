package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.User;
import com.example.loot.Model.UserRecipe;
import com.example.loot.Repository.UserRepository;
import com.example.loot.Repository.UserRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 user recipe not found
//1 success
//2 user not found
//3 recipe name already exists for this user

public class UserRecipeService {

    private final UserRecipeRepository userRecipeRepository;
    private final UserRepository userRepository;


    public List<UserRecipe> getUserRecipes(){
        return userRecipeRepository.findAll();
    }


    public void addUserRecipe(UserRecipe userRecipe){

        User checkUser = userRepository.findUserById(userRecipe.getUserId());

        //user not found
        if(checkUser == null){
            throw new ApiException("User not found");
        }

        UserRecipe checkUserRecipe = userRecipeRepository.findUserRecipeByUserIdAndName(userRecipe.getUserId(), userRecipe.getName());

        //recipe name already exists for this user
        if(checkUserRecipe != null){
            throw new ApiException("Recipe name already exists for this user");
        }

        //added
        userRecipeRepository.save(userRecipe);
    }


    public void editUserRecipe(Integer id, UserRecipe userRecipe){

        UserRecipe oldUserRecipe = userRecipeRepository.findUserRecipeById(id);

        //user recipe not found
        if(oldUserRecipe == null){
            throw new ApiException("User recipe not found");
        }

        UserRecipe nameCheck = userRecipeRepository.findUserRecipeByUserIdAndName(oldUserRecipe.getUserId(), userRecipe.getName());

        //recipe name already taken by another recipe
        if(nameCheck != null && !nameCheck.getId().equals(id)){
            throw new ApiException("Recipe name already exists for this user");
        }

        oldUserRecipe.setName(userRecipe.getName());
        oldUserRecipe.setDescription(userRecipe.getDescription());
        oldUserRecipe.setInstructions(userRecipe.getInstructions());
        oldUserRecipe.setCategory(userRecipe.getCategory());

        //updated
        userRecipeRepository.save(oldUserRecipe);
    }


    public void deleteUserRecipe(int id){

        UserRecipe oldUserRecipe = userRecipeRepository.findUserRecipeById(id);

        //user recipe not found
        if(oldUserRecipe == null){
            throw new ApiException("User recipe not found");
        }

        //deleted
        userRecipeRepository.delete(oldUserRecipe);
    }
}