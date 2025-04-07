package com.example.javitto.controller;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.service.AdvertisementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @PostMapping("/post")
    public ResponseEntity<AdvertisementResponse>createAdv(@RequestBody AdvertisementCreateRequest request) {
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
    public ResponseEntity<AdvertisementResponse> getAdv(@PathVariable Long id) {
        try {
            AdvertisementResponse response = advertisementService.findById(id);
            return ResponseEntity.ok(response);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
