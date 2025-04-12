package com.example.javitto.service.spi;

import com.example.javitto.DTO.NotificationDTO;

public interface NotificationService {
    void send(NotificationDTO notification);
}
