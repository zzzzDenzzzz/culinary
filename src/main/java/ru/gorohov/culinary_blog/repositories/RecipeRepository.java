package ru.gorohov.culinary_blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gorohov.culinary_blog.models.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
