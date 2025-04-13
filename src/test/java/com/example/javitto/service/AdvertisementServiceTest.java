package com.example.javitto.service;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.User;
import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import com.example.javitto.repository.AdvertisementRepository;
import com.example.javitto.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceTest {
    @Mock
    private AdvertisementRepository advertisementRepository;
    @Mock
    private AdvertisementMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityService securityService;
    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private AdvertisementService advertisementService;

    @Test
    void saveAdv_shouldSaveAdvertisement() {
        AdvertisementCreateRequest request = new AdvertisementCreateRequest("Iphone13 pro",
                "Новый, 10 циклов зарядки", BigDecimal.valueOf(10000), ParentCategory.ELECTRONICS,
                SubCategory.SMARTPHONES,
                City.SIMPHEROPOL, "Lenina, 24b", List.of("https://1212"));

        String keycloakId = "8a72cb52-9aac-4fc0-b384-b6df44724354";
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername("testuser111");
        user.setEmail("testuser@example.com111");

        Advertisement savedAdvertisement = Advertisement
                .builder()
                .id(1L)
                .title("Iphone13 pro")
                .description("Новый, 10 циклов зарядки")
                .cost(BigDecimal.valueOf(10000))
                .parentCategory(ParentCategory.ELECTRONICS)
                .subCategory(SubCategory.SMARTPHONES)
                .city(City.SIMPHEROPOL)
                .address("Lenina, 24b")
                .photoUrl(List.of("https://1212"))
                .user(user)
                .dateOfCreation(LocalDateTime.now())
                .build();

        AdvertisementResponse response = new AdvertisementResponse();
        response.setTitle("Iphone13 pro");

        when(securityService.getCurrentUserKeycloakId()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));
        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(savedAdvertisement);
        when(mapper.toResponse(any(Advertisement.class))).thenReturn(response);
        when(mapper.toEntity(request)).thenReturn(savedAdvertisement);

        AdvertisementResponse actualResponse = advertisementService.saveAdv(request);

        assertEquals(response.getTitle(), actualResponse.getTitle());

        verify(emailNotificationService, times(1))
                .sendAdvertisementEmail(user.getEmail(), request.getTitle(), user.getUsername());

    }
}
