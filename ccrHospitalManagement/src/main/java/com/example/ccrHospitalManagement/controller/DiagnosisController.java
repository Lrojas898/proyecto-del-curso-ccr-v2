package com.example.ccrHospitalManagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisDTO> update(@PathVariable Long id, @RequestBody DiagnosisDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    Diagnosis updated = repository.save(existing);
                    return new ResponseEntity<>(mapper.toDto(updated), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

