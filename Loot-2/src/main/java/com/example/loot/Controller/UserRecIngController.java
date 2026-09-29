package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.UserRecIng;
import com.example.loot.Service.UserRecIngService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe/ingredient")
public class UserRecIngController {

    private final UserRecIngService userRecIngService;


    @GetMapping("/get")
    public ResponseEntity<?> getUserRecIng(){
        return ResponseEntity.status(200).body(userRecIngService.getUserRecIng());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUserRecIng(@RequestBody @Valid UserRecIng userRecIng){

        userRecIngService.addUserRecIng(userRecIng);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe ingredient added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editUserRecIng(@PathVariable Integer id, @RequestBody @Valid UserRecIng userRecIng){

        userRecIngService.editUserRecIng(id, userRecIng);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe ingredient updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserRecIng(@PathVariable Integer id){

        userRecIngService.deleteUserRecIng(id);

        return ResponseEntity.status(200).body(new ApiResponse("User recipe ingredient deleted"));
    }
}