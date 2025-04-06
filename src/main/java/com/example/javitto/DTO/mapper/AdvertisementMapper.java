package com.example.javitto.DTO.mapper;

import com.example.javitto.DTO.request.AdvertisementCreateRequest;
import com.example.javitto.DTO.response.AdvertisementResponse;
import com.example.javitto.entity.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {
    @Mapping(source = "user.username", target = "username")
    AdvertisementResponse toResponse(Advertisement advertisement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Advertisement toEntity(AdvertisementCreateRequest request);



}
