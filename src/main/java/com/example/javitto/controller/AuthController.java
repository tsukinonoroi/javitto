package com.example.javitto.controller;


import com.example.javitto.DTO.request.RegistrationRequest;
import com.example.javitto.exception.RegistrationException;
import com.example.javitto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok("юзер зареган");
        }
        catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('client_user')")
    public String hello() {
        return "hello from SB & KC";
    }

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "hello from SB & KC + ADMIN!";
    }
}
