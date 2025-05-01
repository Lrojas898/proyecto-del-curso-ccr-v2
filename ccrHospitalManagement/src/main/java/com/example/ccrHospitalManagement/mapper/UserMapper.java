package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO dto);
}
