package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDto(Role role);
    Role toEntity(RoleDTO dto);
}
