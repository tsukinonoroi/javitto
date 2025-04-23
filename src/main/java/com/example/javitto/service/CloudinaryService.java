package com.example.javitto.service;

import com.example.javitto.entity.Advertisement;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.exception.PhotoNotFoundException;
import com.example.javitto.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final AdvertisementRepository advertisementRepository;
    
    public void addPhotoUrl(Long id, String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL фотографии не может быть пустым");
        }

        try {
            Advertisement ad = advertisementRepository.findById(id)
                    .orElseThrow(() -> new AdvertisementNotFoundException(id));

            ad.getPhotoUrl().add(photoUrl);
            advertisementRepository.save(ad);
            log.info("Фото успешно добавлено к объявлению с ID: {}", id);
        } catch (AdvertisementNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Ошибка при добавлении фото к объявлению с ID: {}", id, ex);
            throw new RuntimeException("Произошла ошибка при добавлении фото: " + ex.getMessage());
        }
    }

    public void removePhoto(Long id, String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL фотографии не может быть пустым");
        }

        try {
            Advertisement ad = advertisementRepository.findById(id)
                    .orElseThrow(() -> new AdvertisementNotFoundException(id));

            if (!ad.getPhotoUrl().contains(photoUrl)) {
                throw new PhotoNotFoundException(photoUrl);
            }

            ad.getPhotoUrl().remove(photoUrl);
            advertisementRepository.save(ad);
            log.info("Фото успешно удалено из объявления с ID: {}", id);
        } catch (AdvertisementNotFoundException | PhotoNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Ошибка при удалении фото из объявления с ID: {}", id, ex);
            throw new RuntimeException("Произошла непредвиденная ошибка: " + ex.getMessage());
        }
    }
}
