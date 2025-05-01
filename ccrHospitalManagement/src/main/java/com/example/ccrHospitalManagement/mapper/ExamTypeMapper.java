package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ExamTypeDTO;
import com.example.ccrHospitalManagement.model.ExamType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamTypeMapper {
    ExamTypeDTO toDto(ExamType type);
    ExamType toEntity(ExamTypeDTO dto);
}
