package com.example.javitto.service;

import com.example.javitto.DTO.request.RegistrationRequest;
import com.example.javitto.exception.RegistrationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final Keycloak keycloakAdminClient;
    private final UserService userService;
    private final EmailNotificationService emailNotificationService;
    private static final String CREDENTIAL_TYPE = CredentialRepresentation.PASSWORD;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.clientId}")
    private String clientId;
    @Value("${keycloak.roleUser}")
    private String roleUser;

    public void registerUser(RegistrationRequest request) {
        UserRepresentation user = createUserRepresentation(request);
        Response response = keycloakAdminClient.realm(realm).users().create(user);

        try {
            handleResponse(response);
            String userId = extractUserIdFromResponse(response);
            assignClientRole(userId);
            log.info("Вытащили юзерид: {}", userId);
            userService.saveUser(userId, request.getUsername(), request.getEmail(), LocalDate.now());
            log.info("Сохраняем пользователя в БД : {} ", request.getUsername());
            /*emailNotificationService.sendRegistrationEmail(request.getEmail(), request.getUsername());*/
        } finally {
            response.close();
        }
    }

    private UserRepresentation createUserRepresentation(RegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(
                new CredentialRepresentation() {{
                    setType(CREDENTIAL_TYPE);
                    setValue(request.getPassword());
                    setTemporary(false);
                }}
        ));
        return user;
    }

    private void assignClientRole(String userId) {
        String clientUUID = keycloakAdminClient.realm(realm)
                .clients()
                .findByClientId(clientId).stream()
                .findFirst()
                .orElseThrow(() -> new RegistrationException("Клиент не найден"))
                .getId();

        RoleRepresentation clientUserRole = keycloakAdminClient.realm(realm)
                .clients().get(clientUUID)
                .roles().get(roleUser)
                .toRepresentation();

        keycloakAdminClient.realm(realm).users().get(userId)
                .roles().clientLevel(clientUUID)
                .add(Collections.singletonList(clientUserRole));
    }

    private void handleResponse(Response response) {
        int status = response.getStatus();
        if (status == 201) {
            return;
        } else if (status == 409) {
            throw new RegistrationException("Имя пользователя или почта уже занята");
        } else {
            throw new RegistrationException("Регистрация не удалась: " + response.readEntity(String.class));
        }
    }

    private String extractUserIdFromResponse(Response response) {
        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            if (location != null) {
                String[] parts = location.split("/");
                return parts[parts.length - 1];
            }
            throw new RegistrationException("Не удалось извлечь ID пользователя из ответа");
        }
        throw new RegistrationException("Пользователь не был создан, статус: " + response.getStatus());
    }


}