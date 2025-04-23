package com.example.javitto.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.exception.PhotoNotFoundException;
import com.example.javitto.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final AdvertisementRepository advertisementRepository;
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public void addPhotoUrl(Long id, String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL фотографии не может быть пустым");
        }

        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));

        ad.getPhotoUrl().add(photoUrl);
        advertisementRepository.save(ad);
        log.info("Фото успешно добавлено к объявлению с ID: {}", id);
    }


    public void removePhoto(Long id, String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL фотографии не может быть пустым");
        }

        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));

        boolean removed = ad.getPhotoUrl().remove(photoUrl);
        if (!removed) {
            throw new PhotoNotFoundException(photoUrl);
        }
        advertisementRepository.save(ad);

        log.info("Фото успешно удалено из объявления с ID: {}", id);
    }

}
