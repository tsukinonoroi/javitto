package com.example.javitto.controller;


import com.example.javitto.DTO.request.RegistrationRequest;
import com.example.javitto.exception.RegistrationException;
import com.example.javitto.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Аутентификация", description = "Регистрация и тестовые эндпоинты для проверки токена")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка регистрации")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationRequest request) {
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            log.error("Реквест null");
        } else {
            log.info("Получен запрос на регистрацию: {}", request);
        }

        try {
            authService.registerUser(request);
            return ResponseEntity.ok("Юзер зареган");
        } catch (RegistrationException e) {
            log.error("Ошибка регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @Operation(summary = "Тестовый endpoint для роли client_user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Доступ разрешён"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/hello")
    @PreAuthorize("hasRole('client_user')")
    public String hello() {
        return "hello from SB & KC";
    }

    @Operation(summary = "Тестовый endpoint для роли client_admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Доступ разрешён"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "hello from SB & KC + ADMIN!";
    }
}
