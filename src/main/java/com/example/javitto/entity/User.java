package com.example.javitto.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDate dateOfRegistration;
}
