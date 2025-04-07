package com.example.javitto.service;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.User;
import com.example.javitto.repository.AdvertisementRepository;
import com.example.javitto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper mapper;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    public AdvertisementResponse saveAdv(AdvertisementCreateRequest request) {
        String keycloakId = securityService.getCurrentUserKeycloakId();

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        log.error("Не найден пользователь с keycloakId" + keycloakId);
        Advertisement advertisement = mapper.toEntity(request);
        advertisement.setUser(user);
        advertisement.setDateOfCreation(LocalDateTime.now());

        advertisementRepository.save(advertisement);

        return mapper.toResponse(advertisement);

    }

    public AdvertisementResponse findById(Long id) {
        Advertisement adv = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        log.error("Не найдено объявление с id " + id);
        return mapper.toResponse(adv);
    }
}


