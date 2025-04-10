package com.example.javitto.service;

import com.example.javitto.DTO.response.NotificationResponse;

public interface NotificationService {
    void send(NotificationResponse notification);
}
