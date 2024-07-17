package ru.gorohov.culinary_blog.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.config.CustomUserDetailsService;
import ru.gorohov.culinary_blog.repositories.UserRepository;
import ru.gorohov.culinary_blog.services.RecipeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private static final String REDIRECT_TO_SHOW_USERS = "redirect:/admin/show-users";
    private final CustomUserDetailsService customUserDetailsService;
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @GetMapping("/show-users")
    public String getShowUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/showUsers";
    }

    @PostMapping("/delete-user/{userId}")
    @Transactional
    public String deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return REDIRECT_TO_SHOW_USERS;
    }

    @PostMapping("/delete-recipe/{recipeId}")
    @Transactional
    public String deleteUserRecipe(@PathVariable Long recipeId) {
        recipeService.delete(recipeId);
        return REDIRECT_TO_SHOW_USERS;
    }

    @PostMapping("/ban-user/{userId}")
    @Transactional
    public String banUser(@PathVariable Long userId) {
        customUserDetailsService.banUser(userId);
        return REDIRECT_TO_SHOW_USERS;
    }

    @PostMapping("/unban-user/{userId}")
    @Transactional
    public String unbanUser(@PathVariable Long userId) {
        customUserDetailsService.unbanUser(userId);
        return REDIRECT_TO_SHOW_USERS;
    }
}
