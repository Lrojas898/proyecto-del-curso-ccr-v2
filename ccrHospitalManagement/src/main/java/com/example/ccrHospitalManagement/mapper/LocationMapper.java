package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.LocationDTO;
import com.example.ccrHospitalManagement.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDTO toDto(Location location);
    Location toEntity(LocationDTO dto);
}
