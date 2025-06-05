package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import com.example.ccrHospitalManagement.mapper.DiagnosisMapper;
import com.example.ccrHospitalManagement.model.Diagnosis;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.DiagnosisRepository;
import com.example.ccrHospitalManagement.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository repository;
    private final DiagnosisMapper mapper;

    @Override
    public List<DiagnosisDTO> findAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    public DiagnosisDTO create(DiagnosisDTO dto, User user) {
        Diagnosis diagnosis = mapper.toEntity(dto);
        System.out.println(diagnosis.toString());
        diagnosis.setCreatedBy(user);
        return mapper.toDto(repository.save(diagnosis));
    }

    @Override
    public DiagnosisDTO update(Long id, DiagnosisDTO dto, User user) {
        return repository.findById(id)
                .map(existing -> {
                    if (!existing.getCreatedBy().getId().equals(user.getId())) {
                        throw new SecurityException("No autorizado para modificar este diagnóstico.");
                    }
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    return mapper.toDto(repository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("Diagnóstico no encontrado"));
    }

    @Override
    public List<DiagnosisDTO> searchByName(String q) {
        return mapper.toDTOList(repository.findByNameContainingIgnoreCase(q));
    }
}
