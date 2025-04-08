package com.example.javitto.repository;


import com.example.javitto.entity.Advertisement;
import com.example.javitto.entity.User;
import com.example.javitto.entity.enums.City;
import com.example.javitto.entity.enums.ParentCategory;
import com.example.javitto.entity.enums.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findByCity(City city);
    Optional<Advertisement> findByTitle(String title);
    Optional<Advertisement> findByParentCategory(ParentCategory parentCategory);
    Optional<Advertisement> findByParentCategoryAndSubCategory(ParentCategory parentCategory, SubCategory subCategory);
    Optional<List<Advertisement>> findByUser_Username(String username);

    Optional<Advertisement> findByUser(User user);

    Optional<Advertisement> findByIdAndUser(Long id, User user);
}
