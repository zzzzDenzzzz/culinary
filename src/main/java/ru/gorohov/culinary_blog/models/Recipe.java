package ru.gorohov.culinary_blog.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipes", indexes = {
        @Index(name = "idx_recipe_name", columnList = "recipeName"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Image URL must be less than 255 characters")
    private String imageUrl;

    private String timeToMake;

    @Size(max = 255, message = "Recipe name must be less than 255 characters")
    private String recipeName;

    private String text;

    @Size(max = 500, message = "Short description must be less than 500 characters")
    private String shortDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "publish_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime publishDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @JoinColumn(name="user_id", nullable = false)
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;
}
