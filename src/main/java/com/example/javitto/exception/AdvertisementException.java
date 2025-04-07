package com.example.javitto.exception;

import jakarta.persistence.EntityNotFoundException;

public class AdvertisementException extends EntityNotFoundException {
    AdvertisementException(String message) {
        super(message);
    }
}
