package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gorohov.culinary_blog.models.Recipe;
import ru.gorohov.culinary_blog.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final RecipeRepository recipeRepository;

    @GetMapping("/")
    public String getHome(Model model,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "3") int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 3;

        Page<Recipe> recipePage = recipeRepository.findAll(PageRequest.of(page - 1, size));
        model.addAttribute("recipePage", recipePage);

        int totalPages = recipePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "home";
    }
}
