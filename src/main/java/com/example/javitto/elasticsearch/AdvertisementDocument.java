package com.example.javitto.elasticsearch;


import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "advertisements")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvertisementDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String title;
    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String description;
    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String parentCategory;
    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String city;
    @Field(type = FieldType.Date)
    private String dateOfCreation;
    @Field(type = FieldType.Double)
    private String cost;
}
