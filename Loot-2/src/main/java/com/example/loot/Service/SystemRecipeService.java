package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.SystemRecipe;
import com.example.loot.Repository.SystemRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//0 system recipe not found
//1 success
//2 system recipe name already taken

public class SystemRecipeService {

    private final SystemRecipeRepository systemRecipeRepository;


    public List<SystemRecipe> getSystemRecipes(){
        return systemRecipeRepository.findAll();
    }


    public void addSystemRecipe(SystemRecipe systemRecipe){

        SystemRecipe oldSystemRecipe = systemRecipeRepository.findSystemRecipeByName(systemRecipe.getName());

        //name taken
        if(oldSystemRecipe != null){
            throw new ApiException("System recipe name already taken");
        }

        //added
        systemRecipeRepository.save(systemRecipe);
    }


    public void editSystemRecipe(Integer id, SystemRecipe systemRecipe){

        SystemRecipe oldSystemRecipe = systemRecipeRepository.findSystemRecipeById(id);

        //system recipe not found
        if(oldSystemRecipe == null){
            throw new ApiException("System recipe not found");
        }

        SystemRecipe nameCheck = systemRecipeRepository.findSystemRecipeByName(systemRecipe.getName());

        //name taken by another system recipe
        if(nameCheck != null && !nameCheck.getId().equals(id)){
            throw new ApiException("System recipe name already taken");
        }

        oldSystemRecipe.setName(systemRecipe.getName());
        oldSystemRecipe.setDescription(systemRecipe.getDescription());
        oldSystemRecipe.setInstructions(systemRecipe.getInstructions());
        oldSystemRecipe.setCategory(systemRecipe.getCategory());

        //updated
        systemRecipeRepository.save(oldSystemRecipe);
    }


    public void deleteSystemRecipe(int id){

        SystemRecipe oldSystemRecipe = systemRecipeRepository.findSystemRecipeById(id);

        //system recipe not found
        if(oldSystemRecipe == null){
            throw new ApiException("System recipe not found");
        }

        //deleted
        systemRecipeRepository.delete(oldSystemRecipe);
    }
}