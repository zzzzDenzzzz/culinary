package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.config.SecurityUtil;
import ru.gorohov.culinary_blog.models.Comment;
import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.services.CommentService;
import ru.gorohov.culinary_blog.services.RecipeService;
import ru.gorohov.culinary_blog.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;

    private final RecipeService recipeService;

    private final CommentService commentService;

    @PostMapping("/add-comment")
    public String postComment(@ModelAttribute Comment comment, @RequestParam Long recipeId){
        Optional<User> userFromSession = SecurityUtil.getUserFromSession();
        Long userId = userFromSession.map(User::getId).orElse(null);
        if(userId == null)
            return "redirect:/recipe/details/" + recipeId;
        User user = userService.findById(userId).orElse(null);
        if(user == null)
            return "redirect:/recipe/details/" + recipeId;
        comment.setUser(user);
        var recipe = recipeService.findById(recipeId).orElse(null);
        if(recipe == null)
            return "redirect:/recipe/details/" + recipeId;
        comment.setRecipe(recipe);
        commentService.save(comment);
        return "redirect:/recipe/details/" + recipeId;
    }
}
