package ru.gorohov.culinary_blog.repositories;

import ru.gorohov.culinary_blog.models.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
}
