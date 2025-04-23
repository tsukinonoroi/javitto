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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Получить пользователя по username",
            description = "Возвращает пользователя по его username",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Не найден с таким username"));

        UserResponse userResponse = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @Operation(
            summary = "Получить текущего аутентифицированного пользователя",
            description = "Возвращает имя текущего пользователя, полученное из токена",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Имя пользователя получено")
    @GetMapping("/user-info")
    public String getUserInfo(Authentication authentication) {
        return authentication.getName();
    }
}

