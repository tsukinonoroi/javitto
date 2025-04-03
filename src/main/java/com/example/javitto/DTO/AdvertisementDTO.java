package com.example.javitto.DTO;

import com.example.javitto.entity.User;
import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class AdvertisementDTO {
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
    private UserDTO user;
}
