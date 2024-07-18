package ru.gorohov.culinary_blog.services;

import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.models.Comment;
import ru.gorohov.culinary_blog.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void save(Comment comment){
        commentRepository.save(comment);
    }
}
