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
    void saveAdv_shouldReturnCorrectResponse_whenAdvertisementCreated() {
        AdvertisementCreateRequest request = new AdvertisementCreateRequest(
                "Iphone13 pro", "Новый, 10 циклов зарядки", BigDecimal.valueOf(10000),
                ParentCategory.ELECTRONICS, SubCategory.SMARTPHONES,
                City.SIMPHEROPOL, "Lenina, 24b", List.of("https://1212"));

        String keycloakId = "8a72cb52-9aac-4fc0-b384-b6df44724354";
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername("testuser111");
        user.setEmail("testuser@example.com111");

        Advertisement advertisement = Advertisement.builder()
                .id(1L)
                .title(request.getTitle())
                .description(request.getDescription())
                .cost(request.getCost())
                .parentCategory(request.getParentCategory())
                .subCategory(request.getSubCategory())
                .city(request.getCity())
                .address(request.getAddress())
                .photoUrl(request.getPhotoUrl())
                .user(user)
                .dateOfCreation(LocalDateTime.now())
                .build();

        AdvertisementResponse expectedResponse = new AdvertisementResponse();
        expectedResponse.setTitle("Iphone13 pro");

        when(securityService.getCurrentUserKeycloakId()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(advertisement);
        when(advertisementRepository.save(any())).thenReturn(advertisement);
        when(mapper.toResponse(any())).thenReturn(expectedResponse);

        AdvertisementResponse actualResponse = advertisementService.saveAdv(request);


        assertEquals(expectedResponse.getTitle(), actualResponse.getTitle());
    }

    @Test
    void saveAdv_shouldSendEmailNotification() {

        AdvertisementCreateRequest request = new AdvertisementCreateRequest(
                "Iphone13 pro", "Новый, 10 циклов зарядки", BigDecimal.valueOf(10000),
                ParentCategory.ELECTRONICS, SubCategory.SMARTPHONES,
                City.SIMPHEROPOL, "Lenina, 24b", List.of("https://1212"));

        String keycloakId = "8a72cb52-9aac-4fc0-b384-b6df44724354";
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername("testuser111");
        user.setEmail("testuser@example.com111");

        Advertisement advertisement = Advertisement.builder()
                .id(1L)
                .title(request.getTitle())
                .user(user)
                .build();

        AdvertisementResponse response = new AdvertisementResponse();
        response.setTitle("Iphone13 pro");

        when(securityService.getCurrentUserKeycloakId()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(advertisement);
        when(advertisementRepository.save(any())).thenReturn(advertisement);
        when(mapper.toResponse(any())).thenReturn(response);

        advertisementService.saveAdv(request);

        verify(emailNotificationService, times(1))
                .sendAdvertisementEmail(user.getEmail(), request.getTitle(), user.getUsername());
    }



}
