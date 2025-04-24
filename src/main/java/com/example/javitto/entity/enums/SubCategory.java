package com.example.javitto.entity.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum SubCategory {
    SMARTPHONES(ParentCategory.ELECTRONICS),
    LAPTOPS(ParentCategory.ELECTRONICS),
    PERSONAL_COMPUTERS(ParentCategory.ELECTRONICS),
    APARTMENT(ParentCategory.REAL_ESTATE),
    HOUSE(ParentCategory.REAL_ESTATE),
    PLOT(ParentCategory.REAL_ESTATE),
    AUTOMOBILE(ParentCategory.TRANSPORT),
    MOTORBIKE(ParentCategory.TRANSPORT),
    BUS(ParentCategory.TRANSPORT);

    private final ParentCategory parentCategory;

    SubCategory(ParentCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public ParentCategory getParentCategory() {
        return parentCategory;
    }
}

