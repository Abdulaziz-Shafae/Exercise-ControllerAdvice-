package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.Ingredient;
import com.example.loot.Service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;


    @GetMapping("/get")
    public ResponseEntity<?> getIngredients(){
        return ResponseEntity.status(200).body(ingredientService.getIngredients());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addIngredient(@RequestBody @Valid Ingredient ingredient){

        ingredientService.addIngredient(ingredient);

        return ResponseEntity.status(200).body(new ApiResponse("Ingredient added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editIngredient(@PathVariable Integer id, @RequestBody @Valid Ingredient ingredient){

        ingredientService.editIngredient(id, ingredient);

        return ResponseEntity.status(200).body(new ApiResponse("Ingredient updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Integer id){

        ingredientService.deleteIngredient(id);

        return ResponseEntity.status(200).body(new ApiResponse("Ingredient deleted"));
    }
}