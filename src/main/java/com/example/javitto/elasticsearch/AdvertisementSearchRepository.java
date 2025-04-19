package com.example.javitto.elasticsearch;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AdvertisementSearchRepository extends ElasticsearchRepository<AdvertisementDocument, Long> {
    List<AdvertisementDocument> findByTitleContainingOrDescriptionContaining(String title, String description);

    
}
