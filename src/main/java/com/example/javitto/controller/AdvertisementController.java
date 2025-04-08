package com.example.javitto.controller;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.request.AdvertisementUpdateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.service.AdvertisementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @PostMapping
    public ResponseEntity<AdvertisementResponse>createAdvertisement(@RequestBody AdvertisementCreateRequest request) {
        try {
           AdvertisementResponse response =
                   advertisementService.saveAdv(request);
           return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> getAdvertisement(@PathVariable Long id) {
        try {
            AdvertisementResponse response = advertisementService.findById(id);
            return ResponseEntity.ok(response);
        }
        catch (AdvertisementNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<AdvertisementResponse>> getAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AdvertisementResponse> advertisements = advertisementService.getAdvertisements(page, size);
        return ResponseEntity.ok(advertisements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> updateAdvertisement(
            @PathVariable Long id,
            @RequestBody AdvertisementUpdateRequest request) {
        AdvertisementResponse response = advertisementService.updateAdvertisement(id, request);

        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }
}
