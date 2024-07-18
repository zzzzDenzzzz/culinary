package ru.gorohov.culinary_blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.gorohov.culinary_blog.dto.RegistrationDTO;
import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping("/login")
    public String getLogin(@RequestParam(required = false) Boolean error, Model model) {
        if (error != null && error) {
            model.addAttribute("loginError", messageSource
                    .getMessage("login.error", null, LocaleContextHolder.getLocale()));
        }
        return "authorization/login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("dto", new RegistrationDTO());
        return "authorization/register";
    }

    @PostMapping("/register/save")
    public String postRegister(@Valid @ModelAttribute("dto") RegistrationDTO dto, HttpServletRequest request, BindingResult result, Model model) {
        if (result.hasErrors() || validateRegisterRequest(result, dto)) {
            return "authorization/register";
        }

        User user = userService.saveFromDto(dto);
        authenticateUser(user, request);
        return "redirect:/";
    }

    private boolean validateRegisterRequest(BindingResult result, RegistrationDTO dto) {
        boolean error = false;
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            result.rejectValue("password", "error.password", messageSource
                    .getMessage("registration.password.required", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (dto.getRepeatPassword() == null || dto.getRepeatPassword().isBlank()) {
            result.rejectValue("repeatPassword", "error.repeatPassword", messageSource
                    .getMessage("registration.repeatPassword.required", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            result.rejectValue("email", "error.email", messageSource
                    .getMessage("registration.email.required", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            result.rejectValue("username", "error.username", messageSource
                    .getMessage("registration.username.required", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (!dto.getPassword().equals(dto.getRepeatPassword())) {
            result.rejectValue("password", "error.passwordMismatch", messageSource
                    .getMessage("registration.password.mismatch", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (userService.isEmailExists(dto.getEmail())) {
            result.rejectValue("email", "error.emailExists", messageSource
                    .getMessage("registration.email.exists", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        if (userService.isUsernameExists(dto.getUsername())) {
            result.rejectValue("username", "error.usernameExists", messageSource
                    .getMessage("registration.username.exists", null, LocaleContextHolder.getLocale()));
            error = true;
        }

        return error;
    }

    private void authenticateUser(User user, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }
}
