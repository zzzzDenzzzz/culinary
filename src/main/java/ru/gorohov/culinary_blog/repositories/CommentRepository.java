package ru.gorohov.culinary_blog.repositories;

import ru.gorohov.culinary_blog.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
}
