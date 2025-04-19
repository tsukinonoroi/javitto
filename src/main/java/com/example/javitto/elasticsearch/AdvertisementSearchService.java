package com.example.javitto.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdvertisementSearchService {
    private final AdvertisementSearchRepository searchRepository;

    public List<AdvertisementDocument> search(String query) {
       return searchRepository.findByTitleContainingOrDescriptionContaining(query, query);
    }

    public void saveToIndex(AdvertisementDocument doc) {
        searchRepository.save(doc);
    }

    public void deleteFromIndex(Long id) {
        searchRepository.deleteById(id);
    }


}
