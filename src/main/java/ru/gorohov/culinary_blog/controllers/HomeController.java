package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final RecipeRepository recipeRepository;

    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("recipes",  recipeRepository.findAll());
        return "home";
    }
}
