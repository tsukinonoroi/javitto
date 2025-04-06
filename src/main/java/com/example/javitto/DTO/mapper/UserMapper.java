package com.example.javitto.DTO.mapper;

import com.example.javitto.DTO.response.UserResponse;
import com.example.javitto.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

}
