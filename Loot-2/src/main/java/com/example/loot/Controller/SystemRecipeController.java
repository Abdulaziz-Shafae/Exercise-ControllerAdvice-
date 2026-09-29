package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.SystemRecipe;
import com.example.loot.Service.SystemRecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system")
public class SystemRecipeController {

    private final SystemRecipeService systemRecipeService;


    @GetMapping("/get")
    public ResponseEntity<?> getSystemRecipes(){
        return ResponseEntity.status(200).body(systemRecipeService.getSystemRecipes());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addSystemRecipe(@RequestBody @Valid SystemRecipe systemRecipe){

        systemRecipeService.addSystemRecipe(systemRecipe);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editSystemRecipe(@PathVariable Integer id, @RequestBody @Valid SystemRecipe systemRecipe){

        systemRecipeService.editSystemRecipe(id, systemRecipe);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSystemRecipe(@PathVariable Integer id){

        systemRecipeService.deleteSystemRecipe(id);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe deleted"));
    }
}