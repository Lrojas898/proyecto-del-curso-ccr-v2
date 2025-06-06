package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.model.Role;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDto(Role role);
    Role toEntity(RoleDTO dto);

    default List<String> map(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    default Set<Role> map(List<String> names) {
        throw new UnsupportedOperationException("Este método debe implementarse manualmente si se usa en mapeo inverso.");
    }
}
