package com.example.javitto.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserDTO {
    private String username;
    private String email;
    private LocalDate dateOfRegistration;
}
