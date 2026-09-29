package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.UserRecipe;
import com.example.loot.Service.UserRecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class UserRecipeController {

    private final UserRecipeService userRecipeService;


    @GetMapping("/get")
    public ResponseEntity<?> getUserRecipes(){
        return ResponseEntity.status(200).body(userRecipeService.getUserRecipes());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUserRecipe(@RequestBody @Valid UserRecipe userRecipe){

        userRecipeService.addUserRecipe(userRecipe);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editUserRecipe(@PathVariable Integer id, @RequestBody @Valid UserRecipe userRecipe){

        userRecipeService.editUserRecipe(id, userRecipe);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserRecipe(@PathVariable Integer id){

        userRecipeService.deleteUserRecipe(id);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe deleted"));
    }
}