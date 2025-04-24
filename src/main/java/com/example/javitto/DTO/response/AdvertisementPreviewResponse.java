package com.example.javitto.DTO.response;

import com.example.javitto.entity.enums.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementPreviewResponse implements Serializable {
    private String title;
    private String description;
    private LocalDateTime dateOfCreation;
    private BigDecimal cost;
    private City city;
}
