package ru.gorohov.culinary_blog.config;

import ru.gorohov.culinary_blog.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {
    private SecurityUtil() {}

    public static Optional<User> getUserFromSession(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
