package com.example.javitto.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.exception.AdvertisementNotFoundException;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdvertisementSearchService {
    private final AdvertisementSearchRepository searchRepository;
    private final AdvertisementMapper mapper;

    public Page<AdvertisementPreviewResponse> searchOrGetAll(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateOfCreation"));

        if (query == null || query.trim().isEmpty()) {
            log.info("Ничего не найдено, т.к. запрос был пустой");
            return Page.empty();
        }

        Page<AdvertisementDocument> searchResults = searchRepository.smartSearch(query, pageable);
        log.info("Найдены документы по запросу {}", query);
        return searchResults.map(mapper::toPreview);
    }

    public Page<AdvertisementPreviewResponse> filter(int page, int size, String parentCategory, String city) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateOfCreation"));

        Page<AdvertisementDocument> documents;

        if (parentCategory != null && city != null) {
            documents = searchRepository.findByParentCategoryAndCity(parentCategory, city, pageable);
        } else if (parentCategory != null) {
            documents = searchRepository.findByParentCategory(parentCategory, pageable);
        } else if (city != null) {
            documents = searchRepository.findByCity(city, pageable);
        } else {
            documents = searchRepository.findAll(pageable);
        }

        if (documents.isEmpty()) {
            throw new AdvertisementNotFoundException("Объявления по указанным фильтрам не найдены");
        }

        return documents.map(mapper::toPreview);
    }


    @Cacheable(value = "latest_ads", key = "#page + '-' + #size")
    public Page<AdvertisementPreviewResponse> getLatest(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateOfCreation"));

        Page<AdvertisementDocument> docs = searchRepository.findAll(pageable);
        return docs.map(mapper::toPreview);
    }

    @CacheEvict(value = "latest_ads", allEntries = true)
    public void saveToIndex(AdvertisementDocument doc) {
        searchRepository.save(doc);
    }

    @CacheEvict(value = "latest_ads", allEntries = true)
    public void deleteFromIndex(Long id) {
        searchRepository.deleteById(id);
    }


}
