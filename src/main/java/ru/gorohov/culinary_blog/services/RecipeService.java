package ru.gorohov.culinary_blog.services;

import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final FileHandler fileHandler;

    @Transactional
    public Optional<Recipe> findById(long id) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.getIngredients().size();
            recipe.getComments().size();
            return recipe;
        });
    }

    @Transactional
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void delete(Long id) {
        var recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) return;
        fileHandler.deleteFile(recipe.getImageUrl());
        recipe.setUser(null);
        recipeRepository.delete(recipe);
    }
}
