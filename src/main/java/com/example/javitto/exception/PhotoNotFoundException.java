package com.example.javitto.exception;


public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(String photoUrl) {
        super("Фото с URL: " + photoUrl + " не найдено");
    }
}
