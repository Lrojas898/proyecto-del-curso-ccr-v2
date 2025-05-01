package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AssistanceActTypeDTO;
import com.example.ccrHospitalManagement.model.AssistanceActType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssistanceActTypeMapper {
    AssistanceActTypeDTO toDto(AssistanceActType type);
    AssistanceActType toEntity(AssistanceActTypeDTO dto);
}