package com.example.javitto.elasticsearch;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface AdvertisementSearchRepository extends ElasticsearchRepository<AdvertisementDocument, Long> {


    Page<AdvertisementDocument> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

    @Query("{\"bool\": { \"should\": [ " +
            "{\"match\": {\"title\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}, " +
            "{\"match\": {\"description\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}} " +
            "]}}")
    Page<AdvertisementDocument> smartSearch(String query, Pageable pageable);

    Page<AdvertisementDocument> findByParentCategory(String parentCategory, Pageable pageable);
    Page<AdvertisementDocument> findByCity(String city, Pageable pageable);
    Page<AdvertisementDocument> findByParentCategoryAndCity(String parentCategory, String city, Pageable pageable);

}
