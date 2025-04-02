package com.example.javitto.entity;

import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table
@Data
@Entity
public class Advertisement {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private ParentCategory parentCategory;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    private LocalDateTime dateOfCreation;
    private String address;
    @ElementCollection
    @CollectionTable(
            name = "advertisement_photos",
            joinColumns = @JoinColumn(name = "advertisement_id")
    )
    @Column(name = "photo_url")
    @OrderColumn(name = "photo_order")
    private List<String> photoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
