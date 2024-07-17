package ru.gorohov.culinary_blog.dto;

import ru.gorohov.culinary_blog.models.Ingredient;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class EditRecipeDTO {
    private long id;

    private MultipartFile file;

    private String timeToMake;

    private String recipeName;

    private String text;

    private String shortDescription;

    private List<Ingredient> ingredients;

    private List<String> newIngredients;
}
