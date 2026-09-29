package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.Model.CookingHistory;
import com.example.loot.Model.User;
import com.example.loot.Repository.CookingHistoryRepository;
import com.example.loot.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
//0 cooking history not found
//1 success
//2 user not found

public class CookingHistoryService {

    private final CookingHistoryRepository cookingHistoryRepository;
    private final UserRepository userRepository;


    public List<CookingHistory> getCookingHistory(){
        return cookingHistoryRepository.findAll();
    }


    public void addCookingHistory(CookingHistory cookingHistory){

        User checkUser = userRepository.findUserById(cookingHistory.getUserId());

        //user not found
        if(checkUser == null){
            throw new ApiException("User not found");
        }

        cookingHistory.setCookedAt(LocalDateTime.now());

        //added
        cookingHistoryRepository.save(cookingHistory);
    }


    public void editCookingHistory(Integer id, CookingHistory cookingHistory){

        CookingHistory oldCookingHistory = cookingHistoryRepository.findCookingHistoryById(id);

        //cooking history not found
        if(oldCookingHistory == null){
            throw new ApiException("Cooking history not found");
        }

        oldCookingHistory.setRecipeName(cookingHistory.getRecipeName());
        oldCookingHistory.setDescription(cookingHistory.getDescription());
        oldCookingHistory.setInstructions(cookingHistory.getInstructions());
        oldCookingHistory.setCategory(cookingHistory.getCategory());
        oldCookingHistory.setRecipeType(cookingHistory.getRecipeType());

        //updated
        cookingHistoryRepository.save(oldCookingHistory);
    }


    public void deleteCookingHistory(int id){

        CookingHistory oldCookingHistory = cookingHistoryRepository.findCookingHistoryById(id);

        //cooking history not found
        if(oldCookingHistory == null){
            throw new ApiException("Cooking history not found");
        }

        //deleted
        cookingHistoryRepository.delete(oldCookingHistory);
    }
}