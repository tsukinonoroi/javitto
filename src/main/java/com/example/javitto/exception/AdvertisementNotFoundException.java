package com.example.javitto.exception;

import jakarta.persistence.EntityNotFoundException;

public class AdvertisementNotFoundException extends EntityNotFoundException {
    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
