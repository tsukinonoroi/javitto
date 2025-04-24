package com.example.javitto.DTO.mapper;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.request.AdvertisementUpdateRequest;
import com.example.javitto.DTO.response.AdvertisementPreviewResponse;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.elasticsearch.AdvertisementDocument;
import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {
    @Mapping(source = "user.username", target = "username")
    AdvertisementResponse toResponse(Advertisement advertisement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Advertisement toEntity(AdvertisementCreateRequest request);

    void updateAdvertisementWithRequest(AdvertisementUpdateRequest request, @MappingTarget Advertisement advertisement);


    @Mapping(target = "parentCategory", expression = "java(mapParentCategory(advertisement.getParentCategory()))")
    @Mapping(target = "city", expression = "java(mapCity(advertisement.getCity()))")
    @Mapping(target = "cost", expression = "java(mapCost(advertisement.getCost()))")
    AdvertisementDocument toDocument(Advertisement advertisement);


    AdvertisementPreviewResponse toPreview(AdvertisementDocument document);





    //helper


    default String mapParentCategory(ParentCategory category) {
        if (category != null) {
            return category.name();
        }
        return null;
    }

    default String mapCity(City city) {
        if (city != null) {
            return city.name();
        }
        return null;
    }

    default String mapCost(BigDecimal cost) {
        if (cost != null) {
            return cost.toString();
        }
        return null;
    }
}
