package com.example.javitto.controller;

import com.example.javitto.DTO.mapper.UserMapper;
import com.example.javitto.DTO.response.UserResponse;
import com.example.javitto.entity.User;
import com.example.javitto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /*@GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Не найден с таким id"));

        UserResponse userResponse = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    } */

    
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Не найден с таким username"));

        UserResponse userResponse = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/user-info")
    public String getUserInfo(Authentication authentication) {
        return authentication.getName();
    }
}
