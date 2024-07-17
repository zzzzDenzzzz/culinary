package ru.gorohov.culinary_blog.services;

import lombok.extern.slf4j.Slf4j;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public void save(Recipe recipe) {
        try {
            recipeRepository.save(recipe);
        } catch (Exception e) {
             log.error("Failed to save recipe", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        recipeRepository.findById(id).ifPresent(recipe -> {
            try {
                fileHandler.deleteFile(recipe.getImageUrl());
            } catch (Exception e) {
                log.error("Failed to delete file: {}", recipe.getImageUrl(), e);
            }
            recipe.setUser(null);
            recipeRepository.delete(recipe);
        });
    }
}
