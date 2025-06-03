
package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ExamResultDTO;
import com.example.ccrHospitalManagement.model.ExamResult;
import com.example.ccrHospitalManagement.repository.ExamTypeRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamResultMapperDecorator {

    private final ExamResultMapper mapper;
    private final UserRepository userRepository;
    private final ExamTypeRepository examTypeRepository;

    public ExamResult toEntity(ExamResultDTO dto) {
        ExamResult entity = mapper.toEntity(dto);

        entity.setPatient(userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado")));

        entity.setTechnician(userRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new IllegalArgumentException("Técnico no encontrado")));

        entity.setExamType(examTypeRepository.findById(dto.getExamTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de examen no encontrado")));

        return entity;
    }

    public ExamResultDTO toDto(ExamResult entity) {
        ExamResultDTO dto = mapper.toDto(entity);

        dto.setPatientId(entity.getPatient().getId());
        dto.setPatientFirstName(entity.getPatient().getFirstName());
        dto.setPatientLastName(entity.getPatient().getLastName());

        dto.setTechnicianId(entity.getTechnician().getId());

        dto.setExamTypeId(entity.getExamType().getId());
        dto.setExamTypeName(entity.getExamType().getName());

        return dto;
    }

}
