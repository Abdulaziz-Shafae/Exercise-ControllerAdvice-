package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.CookingHisIng;
import com.example.loot.Service.CookingHisIngService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history/ingredient")
public class CookingHisIngController {

    private final CookingHisIngService cookingHisIngService;


    @GetMapping("/get")
    public ResponseEntity<?> getCookingHisIng(){
        return ResponseEntity.status(200).body(cookingHisIngService.getCookingHisIng());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addCookingHisIng(@RequestBody @Valid CookingHisIng cookingHisIng){

       cookingHisIngService.addCookingHisIng(cookingHisIng);

        return ResponseEntity.status(200).body(new ApiResponse("Cooking history ingredient added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editCookingHisIng(@PathVariable Integer id, @RequestBody @Valid CookingHisIng cookingHisIng){

        cookingHisIngService.editCookingHisIng(id, cookingHisIng);

        return ResponseEntity.status(200).body(new ApiResponse("Cooking history ingredient updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCookingHisIng(@PathVariable Integer id){

        cookingHisIngService.deleteCookingHisIng(id);

        return ResponseEntity.status(200).body(new ApiResponse("Cooking history ingredient deleted"));
    }
}