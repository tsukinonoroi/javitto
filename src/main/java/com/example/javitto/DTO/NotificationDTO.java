package com.example.javitto.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private String recipient;
    private String subject;
    private String text;
}
