package ru.gorohov.culinary_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRecipeDTO {
    private String timeToMake;

    private String recipeName;

    private String text;

    private MultipartFile file;

    private String shortDescription;

    private List<String> ingredients;
}
