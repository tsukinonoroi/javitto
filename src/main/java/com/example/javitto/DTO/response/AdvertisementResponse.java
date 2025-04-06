package com.example.javitto.DTO.response;

import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal cost;
    private ParentCategory parentCategory;
    private SubCategory subCategory;
    private LocalDateTime dateOfCreation;
    private String address;
    private City city;
    private List<String> photoUrl;
    private String username;
}