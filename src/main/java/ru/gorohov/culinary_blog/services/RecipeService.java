package ru.gorohov.culinary_blog.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final FileHandler fileHandler;
    private final Validator validator;

    @Transactional
    public Optional<Recipe> findById(long id) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.getIngredients().size();
            recipe.getComments().size();
            return recipe;
        });
    }

//    @Transactional
//    public void save(Recipe recipe) {
//        try {
//            recipeRepository.save(recipe);
//        } catch (Exception e) {
//             log.error("Failed to save recipe", e);
//        }
//    }

    @Transactional
    public void save(Recipe recipe) {
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Recipe> violation : violations) {
                sb.append(violation.getMessage()).append(", ");
            }
            throw new ConstraintViolationException("Validation failed: " + sb, violations);
        }

        if (!recipe.hasIngredients()) throw new IllegalArgumentException("Recipe must have at least one ingredient");

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
