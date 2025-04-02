package com.example.javitto.controller;

import com.example.javitto.DTO.UserDTO;
import com.example.javitto.entity.User;
import com.example.javitto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Не найден с таким id"));

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .username(user.getEmail())
                .dateOfRegistration(user.getDateOfRegistration())
                .build();

        return ResponseEntity.ok(userDTO);
    }
}
