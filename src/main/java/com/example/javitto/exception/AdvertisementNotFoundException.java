package com.example.javitto.exception;

import jakarta.persistence.EntityNotFoundException;

public class AdvertisementNotFoundException extends EntityNotFoundException {
    public AdvertisementNotFoundException(Long id) {
        super("Объявление с id: " + id + " не найдено!" );
    }

    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
