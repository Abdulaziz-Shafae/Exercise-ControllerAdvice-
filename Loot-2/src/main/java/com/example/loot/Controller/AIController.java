package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.DTO.*;
import com.example.loot.Service.AIService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;


    @PostMapping("/image/to/ingredient")
    public ResponseEntity<?> imageToIngredient(@RequestParam(value = "image", required = false) MultipartFile image, HttpSession session){

        ImageToIngredientDTO result = aiService.imageToIngredient((Integer) session.getAttribute("userId"), image);

        return ResponseEntity.status(200).body(result);
    }


    @PostMapping("/image/to/ingredient/add")
    public ResponseEntity<?> addImageIngredient(@RequestBody ImageToIngredientDTO imageToIngredientDTO, HttpSession session){

        aiService.addImageIngredient((Integer) session.getAttribute("userId"), imageToIngredientDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Ingredient added to pantry"));
    }


    @PostMapping("/ingredient/substitute/{recipeId}/{listType}/{ingredientId}")
    public ResponseEntity<?> ingredientSubstitute(@PathVariable Integer recipeId, @PathVariable String listType, @PathVariable Integer ingredientId, HttpSession session){

        return ResponseEntity.status(200).body(aiService.ingredientSubstitute((Integer) session.getAttribute("userId"), recipeId, listType, ingredientId));
    }


    @PostMapping("/recipe/recommendation")
    public ResponseEntity<?> recipeRecommendation(@RequestBody @Valid RecipeGeneratorRequestDTO requestDTO, HttpSession session){

        List<AIRecipeDTO> result = aiService.recipeRecommendation((Integer) session.getAttribute("userId"), requestDTO.getRequest());

        return ResponseEntity.status(200).body(result);
    }


    @PostMapping("/leftover/rescue")
    public ResponseEntity<?> leftoverRescue(@RequestBody @Valid List<LeftoverDTO> leftovers, HttpSession session){

        List<AIRecipeDTO> result = aiService.leftoverRescue((Integer) session.getAttribute("userId"), leftovers);

        return ResponseEntity.status(200).body(result);
    }


    @PostMapping("/recipe/generator")
    public ResponseEntity<?> recipeGenerator(@RequestBody @Valid RecipeGeneratorRequestDTO requestDTO, HttpSession session){

        GeneratedRecipeDTO result = aiService.recipeGenerator((Integer) session.getAttribute("userId"), requestDTO.getRequest());

        return ResponseEntity.status(200).body(result);
    }


    @PostMapping("/recipe/generator/add")
    public ResponseEntity<?> addGeneratedRecipe(@RequestBody GeneratedRecipeDTO recipeDTO, HttpSession session){

        aiService.addGeneratedRecipe((Integer) session.getAttribute("userId"), recipeDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Generated recipe added successfully"));
    }
}