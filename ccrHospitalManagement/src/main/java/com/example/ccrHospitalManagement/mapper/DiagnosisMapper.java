package com.example.ccrHospitalManagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import com.example.ccrHospitalManagement.model.Diagnosis;

@Component
public class DiagnosisMapper {

    public DiagnosisDTO toDto(Diagnosis diagnosis) {
        if (diagnosis == null) return null;
        return new DiagnosisDTO(diagnosis.getId(), diagnosis.getName(), diagnosis.getDescription());
    }

    public Diagnosis toEntity(DiagnosisDTO dto) {
        if (dto == null) return null;
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(dto.getId());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
        return diagnosis;
    }

    public List<DiagnosisDTO> toDTOList(List<Diagnosis> diagnoses) {
        return diagnoses.stream().map(this::toDto).collect(Collectors.toList());
    }
}

