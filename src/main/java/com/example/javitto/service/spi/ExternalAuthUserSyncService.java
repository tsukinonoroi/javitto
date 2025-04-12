package com.example.javitto.service.spi;

import org.springframework.security.core.Authentication;

public interface ExternalAuthUserSyncService {
    boolean supports(String provider);
    void syncUserFromToken(Authentication authentication);
}
