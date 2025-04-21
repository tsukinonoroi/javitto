package com.example.javitto.controller;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.request.AdvertisementUpdateRequest;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.elasticsearch.AdvertisementDocument;
import com.example.javitto.elasticsearch.AdvertisementSearchService;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.service.AdvertisementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    private final AdvertisementSearchService searchService;

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

    @GetMapping("/main")
    public ResponseEntity<Page<AdvertisementPreviewResponse>> getMainPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(searchService.getLatest(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdvertisementPreviewResponse>> searchAdvertisements(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(searchService.searchOrGetAll(query, page, size));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> updateAdvertisement(
            @PathVariable Long id, @RequestBody AdvertisementUpdateRequest request) {

        AdvertisementResponse adv = advertisementService.updateAdvertisement(id, request);
        return ResponseEntity.ok().body(adv);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }
}
