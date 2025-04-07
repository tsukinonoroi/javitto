package com.example.javitto.service;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.User;
import com.example.javitto.repository.AdvertisementRepository;
import com.example.javitto.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            String keycloakId = securityService.getCurrentUserKeycloakId();
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

            Advertisement advertisement = mapper.toEntity(request);
            advertisement.setUser(user);
            advertisement.setDateOfCreation(LocalDateTime.now());

            Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
            return mapper.toResponse(savedAdvertisement);
        } catch (EntityNotFoundException e) {
            log.error("Ошибка при сохранении объявления: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при сохранении объявления: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении объявления", e);
        }
    }

    public AdvertisementResponse findById(Long id) {
        try {
            Advertisement adv = advertisementRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
            return mapper.toResponse(adv);
        } catch (EntityNotFoundException e) {
            log.error("Объявление не найдено: id = {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при поиске объявления: id = {}", id, e);
            throw new RuntimeException("Ошибка при поиске объявления", e);
        }
    }
}


