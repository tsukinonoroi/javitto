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
import org.junit.jupiter.api.BeforeEach;
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

    private User user;
    private Advertisement advertisement;
    private AdvertisementCreateRequest request;
    @BeforeEach
    void setup() {
        user = new User();
        user.setKeycloakId("8a72cb52-9aac-4fc0-b384-b6df44724354");
        user.setEmail("testuser@example.com111");
        user.setUsername("testuser111");
        request = AdvertisementCreateRequest
                .builder()
                .title("Iphone13 pro")
                .description("Новый, 10 циклов зарядки")
                .cost(BigDecimal.valueOf(10000))
                .parentCategory(ParentCategory.ELECTRONICS)
                .subCategory(SubCategory.SMARTPHONES)
                .city(City.SIMPHEROPOL)
                .address("Lenina, 24b")
                .photoUrl(List.of("https://1212"))
                .build();

        advertisement = Advertisement.builder()
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
    }

    @Test
    void saveAdv_shouldReturnCorrectResponse_whenAdvertisementCreated() {
        String keycloakId = user.getKeycloakId();

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
        String keycloakId = user.getKeycloakId();

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

    @Test
    void deleteAdv_shouldDeleteAdvByOwner() {
        String keycloakId = user.getKeycloakId();

        when(securityService.getCurrentUserKeycloakId()).thenReturn(keycloakId);
        when(securityService.isAdmin()).thenReturn(false);
        when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));

        advertisementService.deleteAdvertisement(1L);

        verify(advertisementRepository, times(1)).deleteById(1L);

    }

    @Test
    void deleteAdv_shouldDeleteAdvByAdmin() {
        String keycloakId = user.getKeycloakId();

        when(securityService.getCurrentUserKeycloakId()).thenReturn(keycloakId);
        when(securityService.isAdmin()).thenReturn(true);
        when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        advertisementService.deleteAdvertisement(1L);

        verify(advertisementRepository, times(1)).deleteById(1L);
    }

    

}
