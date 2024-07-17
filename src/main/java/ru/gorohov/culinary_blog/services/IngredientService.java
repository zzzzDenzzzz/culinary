package ru.gorohov.culinary_blog.services;

import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.models.Ingredient;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Set<Ingredient> saveAll(List<String> ingredients, Recipe recipe) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredients.forEach(ingredient -> {
            if (!ingredient.isBlank())
                ingredientSet.add(ingredientRepository.save(new Ingredient(0L, ingredient, recipe)));
        });
        return ingredientSet;
    }

    public void deleteAll(List<Ingredient> ingredients) {
        for (var ingredient : ingredients) {
            ingredient.setRecipe(null);
            ingredientRepository.delete(ingredient);
        }
    }
}
