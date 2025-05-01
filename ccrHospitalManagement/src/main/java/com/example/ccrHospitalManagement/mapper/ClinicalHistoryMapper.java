package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicalHistoryMapper {
    ClinicalHistoryDTO toDto(ClinicalHistory history);
    ClinicalHistory toEntity(ClinicalHistoryDTO dto);
}