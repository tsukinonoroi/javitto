package com.example.javitto.controller;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.request.AdvertisementUpdateRequest;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.elasticsearch.AdvertisementSearchService;
import com.example.javitto.exception.AdvertisementNotFoundException;
import com.example.javitto.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/advertisement")
@Tag(name = "Объявления", description = "Операции с объявлениями")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementSearchService searchService;

    @Operation(summary = "Создать новое объявление")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешное создание объявления"),
            @ApiResponse(responseCode = "400", description = "Неверно введенные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    @PostMapping("/")
    public ResponseEntity<AdvertisementResponse> createAdvertisement(
            @RequestBody AdvertisementCreateRequest request) {
        try {
            AdvertisementResponse response = advertisementService.saveAdv(request);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить объявление по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Объявление найдено"),
            @ApiResponse(responseCode = "400", description = "Объявление не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> getAdvertisement(
            @Parameter(description = "ID объявления") @PathVariable Long id) {
        try {
            AdvertisementResponse response = advertisementService.findById(id);
            advertisementService.incrementViews(id);
            return ResponseEntity.ok(response);
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить главную страницу с объявлениями")
    @GetMapping("/main")
    public ResponseEntity<Page<AdvertisementPreviewResponse>> getMainPage(
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(searchService.getLatest(page, size));
    }

    @Operation(summary = "Поиск объявлений")
    @GetMapping("/search")
    public ResponseEntity<Page<AdvertisementPreviewResponse>> searchAdvertisements(
            @Parameter(description = "Поисковый запрос") @RequestParam(required = false) String query,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(searchService.searchOrGetAll(query, page, size));
    }

    @Operation(summary = "Обновить объявление")
    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> updateAdvertisement(
            @Parameter(description = "ID объявления") @PathVariable Long id,
            @RequestBody AdvertisementUpdateRequest request) {
        AdvertisementResponse adv = advertisementService.updateAdvertisement(id, request);
        return ResponseEntity.ok().body(adv);
    }

    @Operation(summary = "Удалить объявление")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(
            @Parameter(description = "ID объявления") @PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }
}

