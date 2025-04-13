package com.example.javitto.entity;

import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "parentCategory", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParentCategory parentCategory;

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    private LocalDateTime dateOfCreation;

    @Column(name = "city", nullable = false)
    @Enumerated(EnumType.STRING)
    private City city;
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
