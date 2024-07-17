package ru.gorohov.culinary_blog.repositories;

import ru.gorohov.culinary_blog.models.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
