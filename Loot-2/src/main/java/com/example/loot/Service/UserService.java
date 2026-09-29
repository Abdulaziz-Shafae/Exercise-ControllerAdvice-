package com.example.loot.Service;

import com.example.loot.Api.ApiException;
import com.example.loot.DTO.*;
import com.example.loot.Model.*;
import com.example.loot.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final PantryItemRepository pantryItemRepository;
    private final UserRecipeRepository userRecipeRepository;
    private final UserRecIngRepository userRecIngRepository;
    private final SystemRecipeRepository systemRecipeRepository;
    private final SystemRecIngRepository systemRecIngRepository;
    private final CookingHistoryRepository cookingHistoryRepository;
    private final CookingHisIngRepository cookingHisIngRepository;
    private final EmailService emailService;


    public List<User> getUsers(){
        return userRepository.findAll();
    }


    public void addUser(User user){

        User oldUser = userRepository.findUserByEmail(user.getEmail());

        //email taken
        if(oldUser != null){
            throw new ApiException("This email is already in use");
        }

        userRepository.save(user);

        //send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
    }


    public void editUser(Integer id, User user){

        User oldUser = userRepository.findUserById(id);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        oldUser.setName(user.getName());
        oldUser.setPassword(user.getPassword());
        oldUser.setPhoneNumber(user.getPhoneNumber());

        userRepository.save(oldUser);
    }


    public void deleteUser(int id){

        User oldUser = userRepository.findUserById(id);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        userRepository.delete(oldUser);
    }


    public Integer login(String email, String password){

        User oldUser = userRepository.findUserByEmail(email);

        //user not found
        if(oldUser == null){
            throw new ApiException("Email not found");
        }

        oldUser = userRepository.findUserByEmailAndPassword(email, password);

        //wrong password
        if(oldUser == null){
            throw new ApiException("Wrong password");
        }

        return oldUser.getId();
    }


    public Integer code;
    private String email;


    public void forgotPassword(String email){

        User oldUser = userRepository.findUserByEmail(email);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        //generate 6 digit random code
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;

        emailService.sendVerificationCode(email, oldUser.getName(), code);

        this.code = code;
        this.email = email;
    }


    public void forgotPasswordCode(String email, String requestEmail, String password, Integer code){

        //email does not match request
        if(!email.equalsIgnoreCase(requestEmail)){
            throw new ApiException("Email does not match");
        }

        //email does not match verification email
        if(this.email == null || !email.equalsIgnoreCase(this.email)){
            throw new ApiException("Email does not match");
        }

        //wrong code
        if(this.code == null || !code.equals(this.code)){
            throw new ApiException("Wrong verification code");
        }

        User oldUser = userRepository.findUserByEmail(email);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        //the password is the same
        if(password.equals(oldUser.getPassword())){
            throw new ApiException("Enter a new password");
        }

        oldUser.setPassword(password);

        userRepository.save(oldUser);

        //clear verification data
        this.email = "";
        this.code = 0;
    }


    public int haveTheIngredient(Integer userId, Integer ingredientId, Double reqQun){

        PantryItem item = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredientId);

        if(item == null){
            return 4;
        }

        if((item.getQuantity() - reqQun) >= 0){
            return 1;
        }

        return 3;
    }


    public int canCookRecipe(Integer userId, Integer recipeId, String listType){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            return 0;
        }

        if(listType.equalsIgnoreCase("user")){

            UserRecipe userRecipe = userRecipeRepository.findUserRecipeByIdAndUserId(recipeId, userId);

            if(userRecipe != null){

                List<UserRecIng> userRecIng = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

                for(int i = 0; i < userRecIng.size(); i++){

                    int result = haveTheIngredient(userId, userRecIng.get(i).getIngredientId(), userRecIng.get(i).getRequiredQuantity());

                    //insufficient ingredient
                    if(result == 3){
                        return 3;
                    }

                    //missing ingredient
                    if(result == 4){
                        return 4;
                    }
                }

                return 1;
            }

            //invalid recipe
            return 6;
        }

        else if(listType.equalsIgnoreCase("system")){

            SystemRecipe systemRecipe = systemRecipeRepository.findSystemRecipeById(recipeId);

            if(systemRecipe != null){

                List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

                for(int i = 0; i < systemRecIngs.size(); i++){

                    int result = haveTheIngredient(userId, systemRecIngs.get(i).getIngredientId(), systemRecIngs.get(i).getRequiredQuantity());

                    //insufficient ingredient
                    if(result == 3){
                        return 3;
                    }

                    //missing ingredient
                    if(result == 4){
                        return 4;
                    }
                }

                return 1;
            }

            //invalid recipe
            return 6;
        }

        //system or user
        return 5;
    }


    public void checkCanCookRecipe(Integer userId, Integer recipeId, String listType){

        int result = canCookRecipe(userId, recipeId, listType);

        //user not found
        if(result == 0){
            throw new ApiException("User not found");
        }

        //insufficient ingredient
        if(result == 3){
            throw new ApiException("Insufficient ingredient quantity");
        }

        //missing ingredient
        if(result == 4){
            throw new ApiException("Missing ingredient");
        }

        //wrong list type
        if(result == 5){
            throw new ApiException("List type must be user or system");
        }

        //recipe not found
        if(result == 6){
            throw new ApiException("Recipe not found");
        }
    }


    public List<MissingIngredientDTO> missingList(Integer userId, Integer recipeId, String listType){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        List<MissingIngredientDTO> list = new ArrayList<>();

        if(listType.equalsIgnoreCase("user")){

            UserRecipe userRecipe = userRecipeRepository.findUserRecipeByIdAndUserId(recipeId, userId);

            //recipe not found
            if(userRecipe == null){
                throw new ApiException("Recipe not found");
            }

            List<UserRecIng> userRecIng = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

            for(int i = 0; i < userRecIng.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIng.get(i).getIngredientId());

                PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredient.getId());

                int result = haveTheIngredient(userId, userRecIng.get(i).getIngredientId(), userRecIng.get(i).getRequiredQuantity());

                //insufficient ingredient
                if(result == 3){

                    MissingIngredientDTO missingIngredientDTO = new MissingIngredientDTO(ingredient.getName(), userRecIng.get(i).getRequiredQuantity(), pantryItem.getQuantity(), userRecIng.get(i).getRequiredQuantity() - pantryItem.getQuantity());

                    list.add(missingIngredientDTO);
                }

                //missing ingredient
                else if(result == 4){

                    MissingIngredientDTO missingIngredientDTO = new MissingIngredientDTO(ingredient.getName(), userRecIng.get(i).getRequiredQuantity(), 0.0, userRecIng.get(i).getRequiredQuantity());

                    list.add(missingIngredientDTO);
                }
            }

            if(list.isEmpty()){
                throw new ApiException("No missing ingredients, you can cook this recipe");
            }

            return list;
        }

        else if(listType.equalsIgnoreCase("system")){

            SystemRecipe systemRecipe = systemRecipeRepository.findSystemRecipeById(recipeId);

            //recipe not found
            if(systemRecipe == null){
                throw new ApiException("Recipe not found");
            }

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

            for(int i = 0; i < systemRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(i).getIngredientId());

                PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredient.getId());

                int result = haveTheIngredient(userId, systemRecIngs.get(i).getIngredientId(), systemRecIngs.get(i).getRequiredQuantity());

                //insufficient ingredient
                if(result == 3){

                    MissingIngredientDTO missingIngredientDTO = new MissingIngredientDTO(ingredient.getName(), systemRecIngs.get(i).getRequiredQuantity(), pantryItem.getQuantity(), systemRecIngs.get(i).getRequiredQuantity() - pantryItem.getQuantity());

                    list.add(missingIngredientDTO);
                }

                //missing ingredient
                else if(result == 4){

                    MissingIngredientDTO missingIngredientDTO = new MissingIngredientDTO(ingredient.getName(), systemRecIngs.get(i).getRequiredQuantity(), 0.0, systemRecIngs.get(i).getRequiredQuantity());

                    list.add(missingIngredientDTO);
                }
            }

            if(list.isEmpty()){
                throw new ApiException("No missing ingredients, you can cook this recipe");
            }

            return list;
        }

        throw new ApiException("List type must be user or system");
    }


    public List<RecipesDTO> getSystemRecipes(){

        List<SystemRecipe> systemRecipes = systemRecipeRepository.findAll();

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < systemRecipes.size(); i++){

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(systemRecipes.get(i).getId());

            List<String> ingredientList = new ArrayList<>();

            for(int j = 0; j < systemRecIngs.size(); j++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(j).getIngredientId());

                ingredientList.add(ingredient.getName() + " - " + systemRecIngs.get(j).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            RecipesDTO recipe = new RecipesDTO(systemRecipes.get(i).getName(), systemRecipes.get(i).getDescription(), systemRecipes.get(i).getInstructions(), systemRecipes.get(i).getCategory(), ingredientList);

            recipes.add(recipe);
        }

        if(recipes.isEmpty()){
            throw new ApiException("No system recipes found");
        }

        return recipes;
    }


    public List<RecipesDTO> getSystemRecipesByCategory(String category){

        validateCategory(category);

        List<SystemRecipe> systemRecipes = systemRecipeRepository.findSystemRecipeByCategory(category);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < systemRecipes.size(); i++){

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(systemRecipes.get(i).getId());

            List<String> ingredientList = new ArrayList<>();

            for(int j = 0; j < systemRecIngs.size(); j++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(j).getIngredientId());

                ingredientList.add(ingredient.getName() + " - " + systemRecIngs.get(j).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            RecipesDTO recipe = new RecipesDTO(systemRecipes.get(i).getName(), systemRecipes.get(i).getDescription(), systemRecipes.get(i).getInstructions(), systemRecipes.get(i).getCategory(), ingredientList);

            recipes.add(recipe);
        }

        if(recipes.isEmpty()){
            throw new ApiException("No system recipes found in this category");
        }

        return recipes;
    }


    public List<RecipesDTO> systemRecipesByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<SystemRecipe> systemRecipes = systemRecipeRepository.findSystemRecipeByCategory(category);

        List<RecipesDTO> list = new ArrayList<>();

        for(int i = 0; i < systemRecipes.size(); i++){

            if(canCookRecipe(userId, systemRecipes.get(i).getId(), "system") == 1){

                List<String> ingredientList = new ArrayList<>();

                List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(systemRecipes.get(i).getId());

                for(int j = 0; j < systemRecIngs.size(); j++){

                    Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(j).getIngredientId());

                    ingredientList.add(ingredient.getName());
                }

                RecipesDTO recipe = new RecipesDTO(systemRecipes.get(i).getName(), systemRecipes.get(i).getDescription(), systemRecipes.get(i).getInstructions(), systemRecipes.get(i).getCategory(), ingredientList);

                list.add(recipe);
            }
        }

        if(list.isEmpty()){
            throw new ApiException("No possible recipes found in this category");
        }

        return list;
    }


    public List<AlmostRecipeDTO> systemRecipesByCategoryAlmost(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<SystemRecipe> systemRecipes = systemRecipeRepository.findSystemRecipeByCategory(category);

        List<AlmostRecipeDTO> list = new ArrayList<>();

        for(int i = 0; i < systemRecipes.size(); i++){

            List<MissingIngredientDTO> missing;

            try{
                missing = missingList(userId, systemRecipes.get(i).getId(), "system");
            }catch(ApiException e){
                if(e.getMessage().equals("No missing ingredients, you can cook this recipe")){
                    continue;
                }
                throw e;
            }

            if(missing.size() >= 1 && missing.size() <= 3){

                List<String> ingredientList = new ArrayList<>();

                List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(systemRecipes.get(i).getId());

                for(int j = 0; j < systemRecIngs.size(); j++){

                    Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(j).getIngredientId());

                    ingredientList.add(ingredient.getName());
                }

                AlmostRecipeDTO recipe = new AlmostRecipeDTO(systemRecipes.get(i).getName(), systemRecipes.get(i).getDescription(), systemRecipes.get(i).getInstructions(), systemRecipes.get(i).getCategory(), ingredientList, missing);

                list.add(recipe);
            }
        }

        if(list.isEmpty()){
            throw new ApiException("No almost possible recipes found in this category");
        }

        return list;
    }


    public List<RecipesDTO> getUserRecipes(Integer userId){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        List<UserRecipe> userRecipes = userRecipeRepository.findUserRecipeByUserId(userId);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < userRecipes.size(); i++){

            List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(userRecipes.get(i).getId());

            List<String> ingredientList = new ArrayList<>();

            for(int j = 0; j < userRecIngs.size(); j++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(j).getIngredientId());

                ingredientList.add(ingredient.getName() + " - " + userRecIngs.get(j).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            RecipesDTO recipe = new RecipesDTO(userRecipes.get(i).getName(), userRecipes.get(i).getDescription(), userRecipes.get(i).getInstructions(), userRecipes.get(i).getCategory(), ingredientList);

            recipes.add(recipe);
        }

        if(recipes.isEmpty()){
            throw new ApiException("No user recipes found");
        }

        return recipes;
    }


    public List<RecipesDTO> getUserRecipesByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<UserRecipe> userRecipes = userRecipeRepository.findUserRecipeByUserIdAndCategory(userId, category);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < userRecipes.size(); i++){

            List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(userRecipes.get(i).getId());

            List<String> ingredientList = new ArrayList<>();

            for(int j = 0; j < userRecIngs.size(); j++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(j).getIngredientId());

                ingredientList.add(ingredient.getName() + " - " + userRecIngs.get(j).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            RecipesDTO recipe = new RecipesDTO(userRecipes.get(i).getName(), userRecipes.get(i).getDescription(), userRecipes.get(i).getInstructions(), userRecipes.get(i).getCategory(), ingredientList);

            recipes.add(recipe);
        }

        if(recipes.isEmpty()){
            throw new ApiException("No user recipes found in this category");
        }

        return recipes;
    }


    public List<RecipesDTO> userRecipesByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<UserRecipe> userRecipes = userRecipeRepository.findUserRecipeByUserIdAndCategory(userId, category);

        List<RecipesDTO> list = new ArrayList<>();

        for(int i = 0; i < userRecipes.size(); i++){

            if(canCookRecipe(userId, userRecipes.get(i).getId(), "user") == 1){

                List<String> ingredientList = new ArrayList<>();

                List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(userRecipes.get(i).getId());

                for(int j = 0; j < userRecIngs.size(); j++){

                    Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(j).getIngredientId());

                    ingredientList.add(ingredient.getName());
                }

                RecipesDTO recipe = new RecipesDTO(userRecipes.get(i).getName(), userRecipes.get(i).getDescription(), userRecipes.get(i).getInstructions(), userRecipes.get(i).getCategory(), ingredientList);

                list.add(recipe);
            }
        }

        if(list.isEmpty()){
            throw new ApiException("No possible user recipes found in this category");
        }

        return list;
    }


    public List<AlmostRecipeDTO> userRecipesByCategoryAlmost(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<UserRecipe> userRecipes = userRecipeRepository.findUserRecipeByUserIdAndCategory(userId, category);

        List<AlmostRecipeDTO> list = new ArrayList<>();

        for(int i = 0; i < userRecipes.size(); i++){

            List<MissingIngredientDTO> missing;

            try{
                missing = missingList(userId, userRecipes.get(i).getId(), "user");
            }catch(ApiException e){
                if(e.getMessage().equals("No missing ingredients, you can cook this recipe")){
                    continue;
                }
                throw e;
            }

            if(missing.size() >= 1 && missing.size() <= 3){

                List<String> ingredientList = new ArrayList<>();

                List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(userRecipes.get(i).getId());

                for(int j = 0; j < userRecIngs.size(); j++){

                    Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(j).getIngredientId());

                    ingredientList.add(ingredient.getName());
                }

                AlmostRecipeDTO recipe = new AlmostRecipeDTO(userRecipes.get(i).getName(), userRecipes.get(i).getDescription(), userRecipes.get(i).getInstructions(), userRecipes.get(i).getCategory(), ingredientList, missing);

                list.add(recipe);
            }
        }

        if(list.isEmpty()){
            throw new ApiException("No almost possible user recipes found in this category");
        }

        return list;
    }


    public List<LowStockDTO> lowStock(Integer userId){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        List<PantryItem> lowStockItems = pantryItemRepository.findLowStockByUserId(userId);

        List<LowStockDTO> list = new ArrayList<>();

        for(int i = 0; i < lowStockItems.size(); i++){

            Ingredient ingredient = ingredientRepository.findIngredientById(lowStockItems.get(i).getIngredientId());

            LowStockDTO lowStockDTO = new LowStockDTO(ingredient.getName(), lowStockItems.get(i).getQuantity() + " " + ingredient.getUnit(), lowStockItems.get(i).getLowStockThreshold() + " " + ingredient.getUnit(), (lowStockItems.get(i).getLowStockThreshold() - lowStockItems.get(i).getQuantity()) + " " + ingredient.getUnit());

            list.add(lowStockDTO);
        }

        if(list.isEmpty()){
            throw new ApiException("No low stock ingredients");
        }

        return list;
    }


    public void lowStockMSG(Integer userId){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        List<LowStockDTO> list = lowStock(userId);

        emailService.sendLowStockEmail(oldUser.getEmail(), oldUser.getName(), list);
    }


    public void removeIngredient(Integer userId, Integer ingredientId, Double reqQun){

        PantryItem item = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredientId);

        item.setQuantity(item.getQuantity() - reqQun);

        pantryItemRepository.save(item);
    }


    public void addCookingHistory(Integer userId, Integer recipeId, String listType){

        CookingHistory cookingHistory = new CookingHistory();

        cookingHistory.setUserId(userId);
        cookingHistory.setRecipeId(recipeId);
        cookingHistory.setCookedAt(LocalDateTime.now());

        if(listType.equalsIgnoreCase("user")){

            UserRecipe userRecipe = userRecipeRepository.findUserRecipeByIdAndUserId(recipeId, userId);

            cookingHistory.setRecipeName(userRecipe.getName());
            cookingHistory.setDescription(userRecipe.getDescription());
            cookingHistory.setInstructions(userRecipe.getInstructions());
            cookingHistory.setCategory(userRecipe.getCategory());
            cookingHistory.setRecipeType("User");

            cookingHistoryRepository.save(cookingHistory);

            List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

            for(int i = 0; i < userRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(i).getIngredientId());

                CookingHisIng cookingHisIng = new CookingHisIng();

                cookingHisIng.setCookingHistoryId(cookingHistory.getId());
                cookingHisIng.setIngredientId(ingredient.getId());
                cookingHisIng.setIngredientName(ingredient.getName());
                cookingHisIng.setUsedQuantity(userRecIngs.get(i).getRequiredQuantity());
                cookingHisIng.setUnit(ingredient.getUnit());

                cookingHisIngRepository.save(cookingHisIng);
            }
        }

        else if(listType.equalsIgnoreCase("system")){

            SystemRecipe systemRecipe = systemRecipeRepository.findSystemRecipeById(recipeId);

            cookingHistory.setRecipeName(systemRecipe.getName());
            cookingHistory.setDescription(systemRecipe.getDescription());
            cookingHistory.setInstructions(systemRecipe.getInstructions());
            cookingHistory.setCategory(systemRecipe.getCategory());
            cookingHistory.setRecipeType("System");

            cookingHistoryRepository.save(cookingHistory);

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

            for(int i = 0; i < systemRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(i).getIngredientId());

                CookingHisIng cookingHisIng = new CookingHisIng();

                cookingHisIng.setCookingHistoryId(cookingHistory.getId());
                cookingHisIng.setIngredientId(ingredient.getId());
                cookingHisIng.setIngredientName(ingredient.getName());
                cookingHisIng.setUsedQuantity(systemRecIngs.get(i).getRequiredQuantity());
                cookingHisIng.setUnit(ingredient.getUnit());

                cookingHisIngRepository.save(cookingHisIng);
            }
        }
    }


    @Transactional
    public void cookRecipe(Integer userId, Integer recipeId, String listType){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        int result = canCookRecipe(userId, recipeId, listType);

        //insufficient ingredient
        if(result == 3){
            throw new ApiException("Insufficient ingredient quantity");
        }

        //missing ingredient
        if(result == 4){
            throw new ApiException("Missing ingredient");
        }

        //wrong list type
        if(result == 5){
            throw new ApiException("List type must be user or system");
        }

        //recipe not found
        if(result == 6){
            throw new ApiException("Recipe not found");
        }

        if(listType.equalsIgnoreCase("user")){

            List<UserRecIng> userRecIng = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

            for(int i = 0; i < userRecIng.size(); i++){
                removeIngredient(userId, userRecIng.get(i).getIngredientId(), userRecIng.get(i).getRequiredQuantity());
            }

            addCookingHistory(userId, recipeId, listType);
        }

        else if(listType.equalsIgnoreCase("system")){

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

            for(int i = 0; i < systemRecIngs.size(); i++){
                removeIngredient(userId, systemRecIngs.get(i).getIngredientId(), systemRecIngs.get(i).getRequiredQuantity());
            }

            addCookingHistory(userId, recipeId, listType);
        }
    }


    public CookingDTO getRecipeForCooking(Integer userId, Integer recipeId, String listType){

        User user = userRepository.findUserById(userId);

        //user not found
        if(user == null){
            throw new ApiException("User not found");
        }

        List<String> ingredients = new ArrayList<>();

        if(listType.equalsIgnoreCase("user")){

            UserRecipe userRecipe = userRecipeRepository.findUserRecipeByIdAndUserId(recipeId, userId);

            //recipe not found
            if(userRecipe == null){
                throw new ApiException("Recipe not found");
            }

            List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

            for(int i = 0; i < userRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(i).getIngredientId());

                ingredients.add(ingredient.getName() + " - " + userRecIngs.get(i).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            List<MissingIngredientDTO> missing = getMissingList(userId, recipeId, "user");

            return new CookingDTO(userRecipe.getName(), userRecipe.getDescription(), userRecipe.getInstructions(), userRecipe.getCategory(), ingredients, missing);
        }

        else if(listType.equalsIgnoreCase("system")){

            SystemRecipe systemRecipe = systemRecipeRepository.findSystemRecipeById(recipeId);

            //recipe not found
            if(systemRecipe == null){
                throw new ApiException("Recipe not found");
            }

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

            for(int i = 0; i < systemRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(i).getIngredientId());

                ingredients.add(ingredient.getName() + " - " + systemRecIngs.get(i).getRequiredQuantity() + " " + ingredient.getUnit());
            }

            List<MissingIngredientDTO> missing = getMissingList(userId, recipeId, "system");

            return new CookingDTO(systemRecipe.getName(), systemRecipe.getDescription(), systemRecipe.getInstructions(), systemRecipe.getCategory(), ingredients, missing);
        }

        throw new ApiException("List type must be user or system");
    }


    public List<RecipesDTO> getHistoryByUserId(Integer userId){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        List<CookingHistory> cookingHistories = cookingHistoryRepository.findCookingHistoryByUserId(userId);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < cookingHistories.size(); i++){

            CookingHistory history = cookingHistories.get(i);

            List<CookingHisIng> cookingHisIngs = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(history.getId());

            List<String> ingredients = new ArrayList<>();

            for(int j = 0; j < cookingHisIngs.size(); j++){

                CookingHisIng ingredient = cookingHisIngs.get(j);

                ingredients.add(ingredient.getIngredientName() + " - " + ingredient.getUsedQuantity() + " " + ingredient.getUnit());
            }

            RecipesDTO recipe = new RecipesDTO(history.getRecipeName(), history.getDescription(), history.getInstructions(), history.getCategory(), ingredients);

            recipes.add(recipe);
        }

        if(recipes.isEmpty()){
            throw new ApiException("No cooking history found");
        }

        return recipes;
    }


    public List<RecipesDTO> getHistoryByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<CookingHistory> cookingHistories = cookingHistoryRepository.findCookingHistoryByUserIdAndCategoryIgnoreCase(userId, category);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < cookingHistories.size(); i++){

            CookingHistory history = cookingHistories.get(i);

            List<CookingHisIng> cookingHisIngs = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(history.getId());

            List<String> ingredients = new ArrayList<>();

            for(int j = 0; j < cookingHisIngs.size(); j++){

                CookingHisIng ingredient = cookingHisIngs.get(j);

                ingredients.add(ingredient.getIngredientName() + " - " + ingredient.getUsedQuantity() + " " + ingredient.getUnit());
            }

            recipes.add(new RecipesDTO(history.getRecipeName(), history.getDescription(), history.getInstructions(), history.getCategory(), ingredients));
        }

        if(recipes.isEmpty()){
            throw new ApiException("No cooking history found in this category");
        }

        return recipes;
    }


    public List<RecipesDTO> possibleHistoryByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<CookingHistory> cookingHistories = cookingHistoryRepository.findCookingHistoryByUserIdAndCategoryIgnoreCase(userId, category);

        List<RecipesDTO> recipes = new ArrayList<>();

        for(int i = 0; i < cookingHistories.size(); i++){

            CookingHistory history = cookingHistories.get(i);

            List<CookingHisIng> cookingHisIngs = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(history.getId());

            boolean canCook = true;

            List<String> ingredients = new ArrayList<>();

            for(int j = 0; j < cookingHisIngs.size(); j++){

                CookingHisIng historyIngredient = cookingHisIngs.get(j);

                int result = haveTheIngredient(userId, historyIngredient.getIngredientId(), historyIngredient.getUsedQuantity());

                if(result != 1){
                    canCook = false;
                    break;
                }

                ingredients.add(historyIngredient.getIngredientName() + " - " + historyIngredient.getUsedQuantity() + " " + historyIngredient.getUnit());
            }

            if(canCook){
                recipes.add(new RecipesDTO(history.getRecipeName(), history.getDescription(), history.getInstructions(), history.getCategory(), ingredients));
            }
        }

        if(recipes.isEmpty()){
            throw new ApiException("No possible history recipes found in this category");
        }

        return recipes;
    }


    public List<AlmostRecipeDTO> almostHistoryByCategory(Integer userId, String category){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        validateCategory(category);

        List<CookingHistory> cookingHistories = cookingHistoryRepository.findCookingHistoryByUserIdAndCategoryIgnoreCase(userId, category);

        List<AlmostRecipeDTO> recipes = new ArrayList<>();

        for(int i = 0; i < cookingHistories.size(); i++){

            CookingHistory history = cookingHistories.get(i);

            List<CookingHisIng> cookingHisIngs = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(history.getId());

            List<String> ingredients = new ArrayList<>();

            List<MissingIngredientDTO> missing = new ArrayList<>();

            for(int j = 0; j < cookingHisIngs.size(); j++){

                CookingHisIng historyIngredient = cookingHisIngs.get(j);

                ingredients.add(historyIngredient.getIngredientName() + " - " + historyIngredient.getUsedQuantity() + " " + historyIngredient.getUnit());

                PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, historyIngredient.getIngredientId());

                //ingredient completely missing
                if(pantryItem == null){

                    missing.add(new MissingIngredientDTO(historyIngredient.getIngredientName(), historyIngredient.getUsedQuantity(), 0.0, historyIngredient.getUsedQuantity()));
                }

                //ingredient exists but quantity is insufficient
                else if(pantryItem.getQuantity() < historyIngredient.getUsedQuantity()){

                    Double missingQuantity = historyIngredient.getUsedQuantity() - pantryItem.getQuantity();

                    missing.add(new MissingIngredientDTO(historyIngredient.getIngredientName(), historyIngredient.getUsedQuantity(), pantryItem.getQuantity(), missingQuantity));
                }
            }

            if(missing.size() >= 1 && missing.size() <= 3){

                recipes.add(new AlmostRecipeDTO(history.getRecipeName(), history.getDescription(), history.getInstructions(), history.getCategory(), ingredients, missing));
            }
        }

        if(recipes.isEmpty()){
            throw new ApiException("No almost possible history recipes found in this category");
        }

        return recipes;
    }


    public CookingDTO getPreviousCook(Integer userId, Integer historyId){

        User user = userRepository.findUserById(userId);

        //user not found
        if(user == null){
            throw new ApiException("User not found");
        }

        CookingHistory history = cookingHistoryRepository.findCookingHistoryByIdAndUserId(historyId, userId);

        //history not found
        if(history == null){
            throw new ApiException("Cooking history not found");
        }

        List<CookingHisIng> historyIngredients = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(historyId);

        List<String> ingredients = new ArrayList<>();

        List<MissingIngredientDTO> missing = new ArrayList<>();

        for(int i = 0; i < historyIngredients.size(); i++){

            CookingHisIng historyIngredient = historyIngredients.get(i);

            ingredients.add(historyIngredient.getIngredientName() + " - " + historyIngredient.getUsedQuantity() + " " + historyIngredient.getUnit());

            PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, historyIngredient.getIngredientId());

            double available = 0.0;

            if(pantryItem != null){
                available = pantryItem.getQuantity();
            }

            if(available < historyIngredient.getUsedQuantity()){

                missing.add(new MissingIngredientDTO(historyIngredient.getIngredientName(), historyIngredient.getUsedQuantity(), available, historyIngredient.getUsedQuantity() - available));
            }
        }

        return new CookingDTO(history.getRecipeName(), history.getDescription(), history.getInstructions(), history.getCategory(), ingredients, missing);
    }


    @Transactional
    public void repeatPreviousCook(Integer userId, Integer historyId){

        User user = userRepository.findUserById(userId);

        //user not found
        if(user == null){
            throw new ApiException("User not found");
        }

        CookingHistory history = cookingHistoryRepository.findCookingHistoryByIdAndUserId(historyId, userId);

        //history not found
        if(history == null){
            throw new ApiException("Cooking history not found");
        }

        List<CookingHisIng> historyIngredients = cookingHisIngRepository.findCookingHisIngByCookingHistoryId(historyId);

        //check all ingredients first
        for(int i = 0; i < historyIngredients.size(); i++){

            int result = haveTheIngredient(userId, historyIngredients.get(i).getIngredientId(), historyIngredients.get(i).getUsedQuantity());

            //insufficient ingredient
            if(result == 3){
                throw new ApiException("Insufficient ingredient quantity");
            }

            //missing ingredient
            if(result == 4){
                throw new ApiException("Missing ingredient");
            }
        }

        //remove ingredients
        for(int i = 0; i < historyIngredients.size(); i++){
            removeIngredient(userId, historyIngredients.get(i).getIngredientId(), historyIngredients.get(i).getUsedQuantity());
        }

        //create new history
        CookingHistory newHistory = new CookingHistory();

        newHistory.setUserId(userId);
        newHistory.setRecipeId(history.getRecipeId());
        newHistory.setRecipeName(history.getRecipeName());
        newHistory.setDescription(history.getDescription());
        newHistory.setInstructions(history.getInstructions());
        newHistory.setCategory(history.getCategory());
        newHistory.setRecipeType(history.getRecipeType());
        newHistory.setCookedAt(LocalDateTime.now());

        cookingHistoryRepository.save(newHistory);

        //copy ingredient snapshot to new history
        for(int i = 0; i < historyIngredients.size(); i++){

            CookingHisIng oldIngredient = historyIngredients.get(i);

            CookingHisIng newIngredient = new CookingHisIng();

            newIngredient.setCookingHistoryId(newHistory.getId());
            newIngredient.setIngredientId(oldIngredient.getIngredientId());
            newIngredient.setIngredientName(oldIngredient.getIngredientName());
            newIngredient.setUsedQuantity(oldIngredient.getUsedQuantity());
            newIngredient.setUnit(oldIngredient.getUnit());

            cookingHisIngRepository.save(newIngredient);
        }
    }


    @Transactional
    public void convertSystemRecipeToUserRecipe(Integer userId, Integer systemRecipeId){

        User oldUser = userRepository.findUserById(userId);

        //user not found
        if(oldUser == null){
            throw new ApiException("User not found");
        }

        SystemRecipe systemRecipe = systemRecipeRepository.findSystemRecipeById(systemRecipeId);

        //system recipe not found
        if(systemRecipe == null){
            throw new ApiException("System recipe not found");
        }

        UserRecipe checkUserRecipe = userRecipeRepository.findUserRecipeByUserIdAndName(userId, systemRecipe.getName());

        //recipe name already exists for this user
        if(checkUserRecipe != null){
            throw new ApiException("Recipe name already exists for this user");
        }

        UserRecipe userRecipe = new UserRecipe();

        userRecipe.setUserId(userId);
        userRecipe.setName(systemRecipe.getName());
        userRecipe.setDescription(systemRecipe.getDescription());
        userRecipe.setInstructions(systemRecipe.getInstructions());
        userRecipe.setCategory(systemRecipe.getCategory());

        userRecipeRepository.save(userRecipe);

        List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(systemRecipeId);

        for(int i = 0; i < systemRecIngs.size(); i++){

            UserRecIng userRecIng = new UserRecIng();

            userRecIng.setUserRecipeId(userRecipe.getId());
            userRecIng.setIngredientId(systemRecIngs.get(i).getIngredientId());
            userRecIng.setRequiredQuantity(systemRecIngs.get(i).getRequiredQuantity());

            userRecIngRepository.save(userRecIng);
        }
    }


    private List<MissingIngredientDTO> getMissingList(Integer userId, Integer recipeId, String listType){

        List<MissingIngredientDTO> list = new ArrayList<>();

        if(listType.equalsIgnoreCase("user")){

            List<UserRecIng> userRecIngs = userRecIngRepository.findUserRecIngByUserRecipeId(recipeId);

            for(int i = 0; i < userRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(userRecIngs.get(i).getIngredientId());

                PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredient.getId());

                int result = haveTheIngredient(userId, userRecIngs.get(i).getIngredientId(), userRecIngs.get(i).getRequiredQuantity());

                if(result == 3){
                    list.add(new MissingIngredientDTO(ingredient.getName(), userRecIngs.get(i).getRequiredQuantity(), pantryItem.getQuantity(), userRecIngs.get(i).getRequiredQuantity() - pantryItem.getQuantity()));
                }

                else if(result == 4){
                    list.add(new MissingIngredientDTO(ingredient.getName(), userRecIngs.get(i).getRequiredQuantity(), 0.0, userRecIngs.get(i).getRequiredQuantity()));
                }
            }
        }

        else if(listType.equalsIgnoreCase("system")){

            List<SystemRecIng> systemRecIngs = systemRecIngRepository.findSystemRecIngBySystemRecipeId(recipeId);

            for(int i = 0; i < systemRecIngs.size(); i++){

                Ingredient ingredient = ingredientRepository.findIngredientById(systemRecIngs.get(i).getIngredientId());

                PantryItem pantryItem = pantryItemRepository.findPantryItemByUserIdAndIngredientId(userId, ingredient.getId());

                int result = haveTheIngredient(userId, systemRecIngs.get(i).getIngredientId(), systemRecIngs.get(i).getRequiredQuantity());

                if(result == 3){
                    list.add(new MissingIngredientDTO(ingredient.getName(), systemRecIngs.get(i).getRequiredQuantity(), pantryItem.getQuantity(), systemRecIngs.get(i).getRequiredQuantity() - pantryItem.getQuantity()));
                }

                else if(result == 4){
                    list.add(new MissingIngredientDTO(ingredient.getName(), systemRecIngs.get(i).getRequiredQuantity(), 0.0, systemRecIngs.get(i).getRequiredQuantity()));
                }
            }
        }

        return list;
    }


    private void validateCategory(String category){

        if(!category.equalsIgnoreCase("breakfast") && !category.equalsIgnoreCase("lunch") && !category.equalsIgnoreCase("dinner") && !category.equalsIgnoreCase("snack")){
            throw new ApiException("Available categories: breakfast, lunch, dinner, snack");
        }
    }
}