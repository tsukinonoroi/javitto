package com.example.javitto.service;

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

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}") private String realm;
    @Value("${keycloak.clientId}") private String clientId;
    @Value("${keycloak.roleUser}") private String roleUser;
    private static final String CREDENTIAL_TYPE = CredentialRepresentation.PASSWORD;
    public String registerUser(String username, String email, String password) {
        UserRepresentation user = createUserRepresentation(username, email, password);
        Response response = keycloak.realm(realm).users().create(user);

        try {
            handleResponse(response);
            return extractUserIdFromResponse(response);
        } finally {
            response.close();
        }
    }

    public void assignDefaultRole(String userId) {
        String clientUUID = getClientUUID();

        RoleRepresentation role = keycloak.realm(realm)
                .clients().get(clientUUID).roles().get(roleUser).toRepresentation();

        keycloak.realm(realm).users().get(userId)
                .roles().clientLevel(clientUUID).add(Collections.singletonList(role));

        log.info("Назначили роль пользователю: {} ", roleUser);
    }

    private UserRepresentation createUserRepresentation(String username, String email, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CREDENTIAL_TYPE);
        credential.setValue(password);
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    private void handleResponse(Response response) {
        int status = response.getStatus();
        if (status == 201) return;
        if (status == 409) throw new RegistrationException("Имя пользователя или почта уже занята");
        throw new RegistrationException("Ошибка Keycloak: " + response.readEntity(String.class));
    }

    private String extractUserIdFromResponse(Response response) {
        String location = response.getHeaderString("Location");
        if (location != null) {
            return location.substring(location.lastIndexOf("/") + 1);
        }
        throw new RegistrationException("Не удалось извлечь ID пользователя");
    }

    private String getClientUUID() {
        return keycloak.realm(realm)
                .clients()
                .findByClientId(clientId).stream()
                .findFirst()
                .orElseThrow(() -> new RegistrationException("Клиент не найден"))
                .getId();
    }
}
