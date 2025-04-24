package com.example.javitto.service;

import com.example.javitto.entity.User;
import com.example.javitto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(String keycloakId, String username, String email, LocalDate dateOfRegistration) {
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername(username);
        user.setEmail(email);
        user.setDateOfRegistration(dateOfRegistration);
        log.info("Сохраняем в БД нового пользователя с username {}", username);
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> getUserById (Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }
}
