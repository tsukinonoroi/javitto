package com.example.javitto.service;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.request.AdvertisementUpdateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.elasticsearch.AdvertisementSearchRepository;
import com.example.javitto.elasticsearch.AdvertisementSearchService;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.User;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.exception.UserNotFoundException;
import com.example.javitto.repository.AdvertisementRepository;
import com.example.javitto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper mapper;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final EmailNotificationService emailNotificationService;
    private final AdvertisementSearchService searchService;
    private final RedisTemplate<String, Object> redis;

    public AdvertisementResponse saveAdv(AdvertisementCreateRequest request) {
        try {
            String keycloakId = securityService.getCurrentUserKeycloakId();
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UserNotFoundException(keycloakId));
            log.info("Найден пользователь: {}", user);
            Advertisement advertisement = mapper.toEntity(request);
            if (advertisement == null) {
                log.error("Маппер не создал объект Advertisement для запроса: {}", request);
                throw new IllegalStateException("Ошибка при создании объекта Advertisement");
            }
            advertisement.setUser(user);
            advertisement.setDateOfCreation(LocalDateTime.now());
            advertisement.setViewsCount(0L);
            Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
            log.info("Сохранили в БД объявление {}",advertisement.getTitle());
            searchService.saveToIndex(mapper.toDocument(savedAdvertisement));
            log.info("Сохранили в документ elasticsearch объявление");

/*
            emailNotificationService.sendAdvertisementEmail(user.getEmail(), advertisement.getTitle(), user.getUsername());
*/

            return mapper.toResponse(savedAdvertisement);
        } catch (RuntimeException e) {
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
                    .orElseThrow(() -> new AdvertisementNotFoundException(id));
            return mapper.toResponse(adv);
        } catch (AdvertisementNotFoundException e) {
            log.error("Объявление не найдено: id = {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при поиске объявления: id = {}", id, e);
            throw new RuntimeException("Ошибка при поиске объявления", e);
        }
    }

    public AdvertisementResponse updateAdvertisement(Long id, AdvertisementUpdateRequest request) {
        Advertisement adv = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));

        String keycloakId = securityService.getCurrentUserKeycloakId();

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException(keycloakId));

        if (!adv.getUser().equals(user) && !securityService.isAdmin()) {
            throw new AccessDeniedException("У вас нет прав на редактирование этого объявления");
        }

        mapper.updateAdvertisementWithRequest(request, adv);

        Advertisement updated = advertisementRepository.save(adv);
        searchService.saveToIndex(mapper.toDocument(updated));

        return mapper.toResponse(updated);
    }
    public void deleteAdvertisement(Long id) {
        String keycloakId = securityService.getCurrentUserKeycloakId();

        Advertisement adv = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));

        if (securityService.isAdmin()) {
            try {
                advertisementRepository.deleteById(id);
                searchService.deleteFromIndex(id);
                log.info("Админ удалил объявление: {}", id);
            } catch (Exception e) {
                log.error("Ошибка при удалении объявления админом: {}", id, e);
                throw new RuntimeException("Ошибка при удалении объявления", e);
            }
            return;
        }
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UserNotFoundException(keycloakId));

            if (!adv.getUser().equals(user)) {
                throw new AccessDeniedException("У вас нет прав для удаления этого объявления");
            }

            try {
                advertisementRepository.deleteById(id);
                searchService.deleteFromIndex(id);
                log.info("Пользователь {} удалил своё объявление: {}", user.getUsername(), id);
            } catch (Exception e) {
                log.error("Ошибка при удалении объявления пользователем: {}", id, e);
                throw new RuntimeException("Ошибка при удалении объявления", e);
        }
    }

    public void incrementViews(Long id) {
        Advertisement adv = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));

        String keycloakId = securityService.getCurrentUserKeycloakId();
        String key = "viewed:" + keycloakId + ":" + id;

        Boolean alreadyViewed = redis.hasKey(key);
        if (Boolean.FALSE.equals(alreadyViewed)) {
            adv.setViewsCount(adv.getViewsCount()+1);
            advertisementRepository.save(adv);

            redis.opsForValue().set(key, "1", Duration.ofHours(1));
        }
    }


}


