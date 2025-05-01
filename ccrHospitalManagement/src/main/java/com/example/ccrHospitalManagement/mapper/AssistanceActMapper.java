package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AssistanceActDTO;
import com.example.ccrHospitalManagement.model.AssistanceAct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssistanceActMapper {
    AssistanceActDTO toDto(AssistanceAct act);
    AssistanceAct toEntity(AssistanceActDTO dto);
}