package com.example.loot.Controller;

import com.example.loot.Api.ApiException;
import com.example.loot.Api.ApiResponse;
import com.example.loot.DTO.*;
import com.example.loot.Model.User;
import com.example.loot.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.status(200).body(userService.getUsers());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user){

        userService.addUser(user);

        return ResponseEntity.status(200).body(new ApiResponse("Account created"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody @Valid User user){

        userService.editUser(id, user);

        return ResponseEntity.status(200).body(new ApiResponse("Information updated"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){

        userService.deleteUser(id);

        return ResponseEntity.status(200).body(new ApiResponse("User deleted"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO, HttpSession session){

        Integer userId = userService.login(loginDTO.getEmail(), loginDTO.getPassword());

        session.setAttribute("userId", userId);

        return ResponseEntity.status(200).body(new ApiResponse("Logged in successfully"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){

        if((Integer) session.getAttribute("userId") == null){
            throw new ApiException("Already logged out");
        }

        session.invalidate();

        return ResponseEntity.status(200).body(new ApiResponse("Logged out successfully"));
    }


    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email){

        userService.forgotPassword(email);

        return ResponseEntity.status(200).body(new ApiResponse("Code sent successfully"));
    }


    @PostMapping("/forgotPassword/{email}/{code}")
    public ResponseEntity<?> forgotPasswordCode(@PathVariable String email, @PathVariable Integer code, @RequestBody @Valid LoginDTO loginDTO){

        userService.forgotPasswordCode(email, loginDTO.getEmail(), loginDTO.getPassword(), code);

        return ResponseEntity.status(200).body(new ApiResponse("Password changed successfully"));
    }


    @GetMapping("/can/{listType}/{recipeId}")
    public ResponseEntity<?> canCookRecipe(@PathVariable String listType, @PathVariable Integer recipeId, HttpSession session){

        userService.checkCanCookRecipe((Integer) session.getAttribute("userId"), recipeId, listType);

        return ResponseEntity.status(200).body(new ApiResponse("You can cook this recipe"));
    }


    @GetMapping("/missing/{listType}/{recipeId}")
    public ResponseEntity<?> missingList(@PathVariable String listType, @PathVariable Integer recipeId, HttpSession session){

        return ResponseEntity.status(200).body(userService.missingList((Integer) session.getAttribute("userId"), recipeId, listType));
    }


    @GetMapping("/system")
    public ResponseEntity<?> getSystemRecipes(){

        return ResponseEntity.status(200).body(userService.getSystemRecipes());
    }


    @GetMapping("/system/{category}")
    public ResponseEntity<?> getSystemRecipesByCategory(@PathVariable String category){

        return ResponseEntity.status(200).body(userService.getSystemRecipesByCategory(category));
    }


    @GetMapping("/possible/system/{category}")
    public ResponseEntity<?> systemRecipesByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.systemRecipesByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/almost/system/{category}")
    public ResponseEntity<?> systemRecipesByCategoryAlmost(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.systemRecipesByCategoryAlmost((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserRecipes(HttpSession session){

        return ResponseEntity.status(200).body(userService.getUserRecipes((Integer) session.getAttribute("userId")));
    }


    @GetMapping("/user/{category}")
    public ResponseEntity<?> getUserRecipesByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.getUserRecipesByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/possible/user/{category}")
    public ResponseEntity<?> userRecipesByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.userRecipesByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/almost/user/{category}")
    public ResponseEntity<?> userRecipesByCategoryAlmost(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.userRecipesByCategoryAlmost((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/low")
    public ResponseEntity<?> lowStock(HttpSession session){

        return ResponseEntity.status(200).body(userService.lowStock((Integer) session.getAttribute("userId")));
    }


    @PostMapping("/low/email")
    public ResponseEntity<?> lowStockEmail(HttpSession session){

        userService.lowStockMSG((Integer) session.getAttribute("userId"));

        return ResponseEntity.status(200).body(new ApiResponse("Low stock list sent to your email successfully"));
    }


    @GetMapping("/cook/{listType}/{recipeId}")
    public ResponseEntity<?> cookRecipe(@PathVariable String listType, @PathVariable Integer recipeId, HttpSession session){

        return ResponseEntity.status(200).body(userService.getRecipeForCooking((Integer) session.getAttribute("userId"), recipeId, listType));
    }


    @PostMapping("/cook/{listType}/{recipeId}/done")
    public ResponseEntity<?> cookRecipeDone(@PathVariable String listType, @PathVariable Integer recipeId, HttpSession session){

        userService.cookRecipe((Integer) session.getAttribute("userId"), recipeId, listType);

        return ResponseEntity.status(200).body(new ApiResponse("Recipe cooked successfully"));
    }


    @GetMapping("/history")
    public ResponseEntity<?> getHistoryByUserId(HttpSession session){

        return ResponseEntity.status(200).body(userService.getHistoryByUserId((Integer) session.getAttribute("userId")));
    }


    @GetMapping("/history/{category}")
    public ResponseEntity<?> getHistoryByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.getHistoryByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/possible/history/{category}")
    public ResponseEntity<?> possibleHistoryByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.possibleHistoryByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/almost/history/{category}")
    public ResponseEntity<?> almostHistoryByCategory(@PathVariable String category, HttpSession session){

        return ResponseEntity.status(200).body(userService.almostHistoryByCategory((Integer) session.getAttribute("userId"), category));
    }


    @GetMapping("/history/{historyId}/repeat")
    public ResponseEntity<?> getPreviousCook(@PathVariable Integer historyId, HttpSession session){

        return ResponseEntity.status(200).body(userService.getPreviousCook((Integer) session.getAttribute("userId"), historyId));
    }


    @PostMapping("/history/{historyId}/repeat/done")
    public ResponseEntity<?> repeatPreviousCook(@PathVariable Integer historyId, HttpSession session){

        userService.repeatPreviousCook((Integer) session.getAttribute("userId"), historyId);

        return ResponseEntity.status(200).body(new ApiResponse("Previous recipe cooked successfully"));
    }


    @PostMapping("/system/{recipeId}/convert")
    public ResponseEntity<?> convertSystemRecipeToUserRecipe(@PathVariable Integer recipeId, HttpSession session){

        userService.convertSystemRecipeToUserRecipe((Integer) session.getAttribute("userId"), recipeId);

        return ResponseEntity.status(200).body(new ApiResponse("System recipe converted to user recipe successfully"));
    }
}