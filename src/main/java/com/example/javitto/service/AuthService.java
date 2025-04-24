package com.example.javitto.service;

import com.example.javitto.DTO.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final Keycloak keycloakAdminClient;
    private final UserService userService;
    private final EmailNotificationService emailNotificationService;
    private final KeycloakUserService keycloakUserService;
    private static final String CREDENTIAL_TYPE = CredentialRepresentation.PASSWORD;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.clientId}")
    private String clientId;
    @Value("${keycloak.roleUser}")
    private String roleUser;

    public void registerUser(RegistrationRequest request) {
        String userId = keycloakUserService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        keycloakUserService.assignDefaultRole(userId);
        userService.saveUser(userId, request.getUsername(), request.getEmail(), LocalDate.now());
        emailNotificationService.sendRegistrationEmail(request.getEmail(), request.getUsername());
    }




}