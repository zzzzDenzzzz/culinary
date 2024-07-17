package ru.gorohov.culinary_blog.controllers;

import ru.gorohov.culinary_blog.dto.EditRecipeDTO;
import ru.gorohov.culinary_blog.dto.PostRecipeDTO;
import ru.gorohov.culinary_blog.config.SecurityUtil;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.services.FileHandler;
import ru.gorohov.culinary_blog.services.IngredientService;
import ru.gorohov.culinary_blog.services.RecipeService;
import ru.gorohov.culinary_blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    private final UserService userService;

    private final IngredientService ingredientService;

    private final FileHandler fileHandler;

    @GetMapping("/details/{id}")
    public String getRecipeById(@PathVariable Long id, Model model) {
        return recipeService.findById(id)
                .map(recipe -> {
                    model.addAttribute("recipe", recipe);
                    return "recipe/recipeDetails";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/add")
    public String getAddRecipe(Model model)
    {
        return "recipe/addRecipe";
    }

    @GetMapping("/edit/{id}")
    public String getEditRecipe(@PathVariable Long id, Model model) {
        return recipeService.findById(id)
                .map(recipe -> {
                    EditRecipeDTO dto = mapToEditRecipeDTO(recipe);
                    model.addAttribute("dto", dto);
                    return "recipe/editRecipe";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/edit/{id}")
    public String postEditRecipe(@PathVariable Long id, @ModelAttribute EditRecipeDTO dto, Model model) {
        return recipeService.findById(id)
                .map(recipe -> {
                    updateRecipeFromDTO(recipe, dto);
                    recipeService.save(recipe);
                    return "redirect:/";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/delete/{id}")
    public String postDelete(@PathVariable Long id, Model model) {
        recipeService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String postAddRecipe(@ModelAttribute PostRecipeDTO dto, Model model) {
        String imageUrl = fileHandler.storeFile(dto.getFile());
        if (imageUrl == null) return "recipe/addRecipe";

        Recipe recipe = new Recipe();
        updateRecipeFromDTO(recipe, dto, imageUrl);

        return SecurityUtil.getUserFromSession()
                .map(userFromSession -> {
                    Long userId = userFromSession.getId();
                    User user = userService.findById(userId).orElse(null);
                    if (user == null) return "recipe/addRecipe";
                    recipe.setUser(user);
                    recipeService.save(recipe);
                    ingredientService.saveAll(dto.getIngredients(), recipe);
                    return "redirect:/";
                })
                .orElse("recipe/addRecipe");
    }

    private EditRecipeDTO mapToEditRecipeDTO(Recipe recipe) {
        return EditRecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .text(recipe.getText())
                .timeToMake(recipe.getTimeToMake())
                .shortDescription(recipe.getShortDescription())
                .ingredients(recipe.getIngredients())
                .build();
    }

    private void updateRecipeFromDTO(Recipe recipe, EditRecipeDTO dto) {
        recipe.setRecipeName(dto.getRecipeName());
        recipe.setText(dto.getText());
        recipe.setTimeToMake(dto.getTimeToMake());
        recipe.setShortDescription(dto.getShortDescription());
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String imageUrl = fileHandler.storeFile(dto.getFile());
            recipe.setImageUrl(imageUrl);
        }
    }

    private void updateRecipeFromDTO(Recipe recipe, PostRecipeDTO dto, String imageUrl) {
        recipe.setRecipeName(dto.getRecipeName());
        recipe.setTimeToMake(dto.getTimeToMake());
        recipe.setText(dto.getText());
        recipe.setShortDescription(dto.getShortDescription());
        recipe.setImageUrl(imageUrl);
    }
}
