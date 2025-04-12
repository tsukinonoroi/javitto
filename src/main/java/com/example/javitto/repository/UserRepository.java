package com.example.javitto.repository;


import com.example.javitto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByKeycloakId(String keycloakId);

    boolean existsByKeycloakId(String keycloakId);
}
