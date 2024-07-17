package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String postComment(@ModelAttribute Comment comment, @RequestParam Long recipeId, RedirectAttributes redirectAttributes) {
        Optional<User> userFromSession = SecurityUtil.getUserFromSession();
        Long userId = userFromSession.map(User::getId).orElse(null);
        if (userId == null) {
            return redirectToRecipeDetails(recipeId, redirectAttributes);
        }

        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        var recipe = recipeService.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Рецепт не найден"));

        comment.setUser(user);
        comment.setRecipe(recipe);
        commentService.save(comment);

        return "redirect:/recipe/details/" + recipeId;
    }

    private String redirectToRecipeDetails(Long recipeId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Пользователь не найден");
        return "redirect:/recipe/details/" + recipeId;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/";
    }
}
