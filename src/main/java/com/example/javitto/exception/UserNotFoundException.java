package com.example.javitto.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String keycloakId) {
        super("Пользователь с id: " + keycloakId + " не найден!");
    }
}
