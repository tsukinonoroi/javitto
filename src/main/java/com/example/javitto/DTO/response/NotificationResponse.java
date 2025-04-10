package com.example.javitto.DTO.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private String recipient;
    private String subject;
    private String text;
}
