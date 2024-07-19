package ru.gorohov.culinary_blog.interceptor;

import lombok.NonNull;
import ru.gorohov.culinary_blog.config.SecurityUtil;
import ru.gorohov.culinary_blog.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        try {
            Optional<User> optionalUser = SecurityUtil.getUserFromSession();
            optionalUser.ifPresentOrElse(
                    user -> addUserAttributesToRequest(request, user),
                    () -> log.debug("В сеансе не найдено ни одного пользователя.")
            );
        } catch (Exception e) {
            log.error("Ошибка при извлечении пользователя из сеанса. {}", e.getMessage());
        }
        return true;
    }

    private void addUserAttributesToRequest(HttpServletRequest request, User user) {
        request.setAttribute("username", user.getUsername());
        request.setAttribute("userId", user.getId());
        request.setAttribute("role", user.getRole().name());
    }
}
