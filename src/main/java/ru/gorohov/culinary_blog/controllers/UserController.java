package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.services.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile/{userId}")
    public String viewProfile(@PathVariable Long userId, Model model) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("user", user.get());

        return "/user/profile";
    }
}
