package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ExamResultDetailDTO;
import com.example.ccrHospitalManagement.model.ExamResultDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamResultDetailMapper {
    ExamResultDetailDTO toDto(ExamResultDetail detail);
    ExamResultDetail toEntity(ExamResultDetailDTO dto);
    List<ExamResultDetailDTO> toDtoList(List<ExamResultDetail> details);
    List<ExamResultDetail> toEntityList(List<ExamResultDetailDTO> dtos);
}
