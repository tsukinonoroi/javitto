package com.example.javitto.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.clientId}")
    private String clientId;
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm(realm)
                .clientId(clientId)
                .clientSecret("rE0ySYD6QQyMe7xD8EJ8EA373NvAPOpc")
                .grantType("client_credentials")
                .build();
    }
}
