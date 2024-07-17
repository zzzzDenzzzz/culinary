package ru.gorohov.culinary_blog.services;

import lombok.RequiredArgsConstructor;
import ru.gorohov.culinary_blog.dto.RegistrationDTO;
import ru.gorohov.culinary_blog.models.Role;
import ru.gorohov.culinary_blog.models.User;
import ru.gorohov.culinary_blog.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User saveFromDto(RegistrationDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
       return userRepository.save(user);
    }

    public Optional<User> findFirstByUsername(String username){
        return userRepository.findFirstByUsername(username);
    }
}
