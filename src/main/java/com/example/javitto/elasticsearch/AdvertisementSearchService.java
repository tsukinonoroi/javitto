package com.example.javitto.elasticsearch;

import com.example.javitto.DTO.mapper.AdvertisementMapper;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import lombok.RequiredArgsConstructor;
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
            Page<AdvertisementDocument> docs = searchRepository.findAll(pageable);
            return docs.map(mapper::toPreview);
        }

        Page<AdvertisementDocument> searchResults = searchRepository
                .findByTitleContainingOrDescriptionContaining(query, query, pageable);

        return searchResults.map(mapper::toPreview);
    }


    public void saveToIndex(AdvertisementDocument doc) {
        searchRepository.save(doc);
    }

    public void deleteFromIndex(Long id) {
        searchRepository.deleteById(id);
    }


}
