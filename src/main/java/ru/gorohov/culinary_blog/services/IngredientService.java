package ru.gorohov.culinary_blog.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.models.Ingredient;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void saveAll(List<String> ingredients, Recipe recipe) {
        if (isNull(ingredients)) return;

        final var ingredientSet = getIngredients(ingredients, recipe);

        saveIngredientsIsNotEmpty(ingredientSet);
    }

    private static Set<Ingredient> getIngredients(List<String> ingredients, Recipe recipe) {
        return ingredients
                .stream()
                .filter(ingredient -> !ingredient.isBlank())
                .map(ingredient -> new Ingredient(0L, ingredient, recipe))
                .collect(Collectors.toSet());
    }

    private void saveIngredientsIsNotEmpty(Set<Ingredient> ingredientSet) {
        if (!ingredientSet.isEmpty()) {
            ingredientRepository.saveAll(ingredientSet);
        }
    }

    private static boolean isNull(List<String> ingredients) {
        return ingredients == null || ingredients.isEmpty();
    }
}
