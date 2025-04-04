package com.example.javitto.service;

import com.example.javitto.entity.Advertisement;
import com.example.javitto.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    public Advertisement saveAdv(Advertisement advertisement) {

            advertisement.setDateOfCreation(LocalDateTime.now());
            return advertisementRepository.save(advertisement);
    }

}
