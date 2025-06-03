package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ExamResultDTO;
import com.example.ccrHospitalManagement.model.ExamResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ExamResultDetailMapper.class })
public interface ExamResultMapper {
    ExamResultDTO toDto(ExamResult result);
    ExamResult toEntity(ExamResultDTO dto);
}
