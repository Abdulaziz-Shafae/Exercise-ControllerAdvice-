package com.example.loot.Controller;

import com.example.loot.Api.ApiResponse;
import com.example.loot.Model.SystemRecIng;
import com.example.loot.Service.SystemRecIngService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system/ingredient")
public class SystemRecIngController {

    private final SystemRecIngService systemRecIngService;


    @GetMapping("/get")
    public ResponseEntity<?> getSystemRecIngs(){
        return ResponseEntity.status(200).body(systemRecIngService.getSystemRecIngs());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addSystemRecIng(@RequestBody @Valid SystemRecIng systemRecIng){

        systemRecIngService.addSystemRecIng(systemRecIng);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe ingredient added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editSystemRecIng(@PathVariable Integer id, @RequestBody @Valid SystemRecIng systemRecIng){

        systemRecIngService.editSystemRecIng(id, systemRecIng);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe ingredient updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSystemRecIng(@PathVariable Integer id){

        systemRecIngService.deleteSystemRecIng(id);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe ingredient deleted"));
    }
}