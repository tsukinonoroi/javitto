package com.example.javitto.elasticsearch;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdvertisementSearchService {
    private final AdvertisementSearchRepository searchRepository;
    private final AdvertisementMapper mapper;

    public Page<AdvertisementPreviewResponse> searchOrGetAll(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateOfCreation"));

        if (query == null || query.trim().isEmpty()) {
            getLatest(page, size);
        }

        Page<AdvertisementDocument> searchResults = searchRepository
                .findByTitleContainingOrDescriptionContaining(query, query, pageable);

        return searchResults.map(mapper::toPreview);
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
