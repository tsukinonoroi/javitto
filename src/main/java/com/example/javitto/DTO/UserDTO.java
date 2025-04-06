package com.example.javitto.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Data
@Getter
public class UserDTO {
    private String username;
    private String email;
    private LocalDate dateOfRegistration;
}
