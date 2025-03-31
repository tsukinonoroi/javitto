package com.example.javitto.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}