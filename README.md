# Loot | لوت
## Controller & Service Refactoring

This update cleans the backend structure and applies the new controller/service template across the project.

## Main Changes

- Controllers are now smaller and only handle requests, service calls, and successful responses.
- Removed `Errors` and manual validation checks from controllers.
- `@Valid` validation errors are handled by `ControllerAdvice`.
- Services now throw `ApiException` instead of returning error codes for normal endpoint errors.
- CRUD add/edit/delete methods were changed to `void` where no return value is needed.
- `ControllerAdvice` handles `ApiException` and validation errors globally.
- `ControllerAdvice` should be inside:
  `com.example.loot.Advice`
- Category validation was moved into one reusable service method.
- Empty list results such as no recipes, no history, or no low-stock items are handled inside the service.
- `@Transactional` is kept for operations that update multiple database records.

## UserService

`UserService` was updated for:

- User CRUD
- Login and forgot password
- Missing ingredients
- Possible and almost possible recipes
- Low stock
- Cooking and cooking history
- Repeat previous cook
- Convert system recipe to user recipe

`haveTheIngredient()` and `canCookRecipe()` still use numeric status codes because they are used internally by recipe and AI logic.

A private `getMissingList()` helper is used when an empty missing list is valid internally.

## AIService

The AI endpoints now follow the same pattern:

- Image to Ingredient
- Add Image Ingredient
- Ingredient Substitute
- Recipe Recommendation
- Leftover Rescue
- Recipe Generator
- Add Generated Recipe

AI validation and errors are handled inside `AIService` using `ApiException`.

## Final Structure

```text
Controller
→ Receives request
→ Calls service
→ Returns successful response

Service
→ Business logic
→ Validation
→ Throws ApiException

ControllerAdvice
→ Handles exceptions
→ Returns error responses
```

This makes the Loot | لوت backend cleaner, more consistent, and easier to maintain.
