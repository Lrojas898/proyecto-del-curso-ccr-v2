package com.example.ccrHospitalManagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ccrHospitalManagement.mapper.DiagnosisMapper;
import com.example.ccrHospitalManagement.model.Diagnosis;
import com.example.ccrHospitalManagement.repository.DiagnosisRepository;
import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import lombok.*;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisRepository repository;
    private final DiagnosisMapper mapper;

    @GetMapping
    public List<DiagnosisDTO> findAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisDTO> create(@RequestBody DiagnosisDTO dto) {
        Diagnosis saved = repository.save(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public List<DiagnosisDTO> search(@RequestParam String q) {
        return mapper.toDTOList(repository.findByNameContainingIgnoreCase(q));
    }
}

