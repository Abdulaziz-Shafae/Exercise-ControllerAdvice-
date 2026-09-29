package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.PantryItem;
import com.example.loot.Service.PantryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pantry")
public class PantryItemController {

    private final PantryItemService pantryItemService;


    @GetMapping("/get")
    public ResponseEntity<?> getPantryItems(){
        return ResponseEntity.status(200).body(pantryItemService.getPantryItems());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addPantryItem(@RequestBody @Valid PantryItem pantryItem){

        pantryItemService.addPantryItem(pantryItem);

        return ResponseEntity.status(200).body(new ApiResponse("Pantry item added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editPantryItem(@PathVariable Integer id, @RequestBody @Valid PantryItem pantryItem){

        pantryItemService.editPantryItem(id, pantryItem);

        return ResponseEntity.status(200).body(new ApiResponse("Pantry item updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePantryItem(@PathVariable Integer id){

        pantryItemService.deletePantryItem(id);

        return ResponseEntity.status(200).body(new ApiResponse("Pantry item deleted"));
    }
}