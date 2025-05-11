package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClinicalHistoryMapperDecorator {

    private final ClinicalHistoryMapper mapper;
    private final UserRepository userRepository;

    public ClinicalHistory toEntity(ClinicalHistoryDTO dto) {
        ClinicalHistory entity = mapper.toEntity(dto);

        // ⬇️ Aquí agregas la línea
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario asignado no es válido.")));

        return entity;
    }

    public ClinicalHistoryDTO toDto(ClinicalHistory entity) {
        return mapper.toDto(entity);
    }
}
