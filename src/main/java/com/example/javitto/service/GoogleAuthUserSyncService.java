package com.example.javitto.service;

import com.example.javitto.service.UserService;
import com.example.javitto.service.spi.ExternalAuthUserSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthUserSyncService implements ExternalAuthUserSyncService {
    private final UserService userService;

    @Override
    public boolean supports(String provider) {
        return "google".equalsIgnoreCase(provider);
    }

    @Override
    public void syncUserFromToken(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwt) {
            String keycloakId = jwt.getToken().getSubject();
            String username = jwt.getToken().getClaimAsString("preferred_username");
            String email = jwt.getToken().getClaimAsString("email");

            if (!userService.existsByKeycloakId(keycloakId)) {
                userService.saveUser(keycloakId, username, email, LocalDate.now());
            }
        }
    }
}
