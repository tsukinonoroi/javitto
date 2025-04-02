package com.example.javitto.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table
@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String keycloakId;

    private String username;
}
