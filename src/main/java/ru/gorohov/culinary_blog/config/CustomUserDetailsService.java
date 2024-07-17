package ru.gorohov.culinary_blog.config;

import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findFirstByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Пользователь с таким именем " + username + " не найден"));
    }

    public void banUser(Long id) {
        findUserById(id).ifPresent(user -> {
            user.setAccountNonLocked(false);
            try {
                userRepository.save(user);
                log.info("Пользователь с идентификатором {} забанен", id);
            } catch (Exception e) {
                log.error("При попытке забанить пользователя с идентификатором {} произошла ошибка {}", id, e.getMessage());
            }
        });
    }

    public void unbanUser(Long id) {
        findUserById(id).ifPresent(user -> {
            user.setAccountNonLocked(true);
            try {
                userRepository.save(user);
                log.info("Пользователь с идентификатором {} разбанен", id);
            } catch (Exception e) {
                log.error("При попытке разбанить пользователя с идентификатором {} произошла ошибка {}", id, e.getMessage());
            }
        });
    }

    private Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
