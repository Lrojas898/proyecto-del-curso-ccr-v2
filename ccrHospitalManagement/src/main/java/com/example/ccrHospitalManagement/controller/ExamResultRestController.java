package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ExamResultDTO;
import com.example.ccrHospitalManagement.mapper.ExamResultMapper;
import com.example.ccrHospitalManagement.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-results")
@RequiredArgsConstructor
public class ExamResultRestController {

    private final ExamResultService service;
    private final ExamResultMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','TÉCNICO DE LABORATORIO')")
    public List<ExamResultDTO> getAll() {
        return service.getAllExamResults().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','TÉCNICO DE LABORATORIO','PACIENTE')")
    public ResponseEntity<ExamResultDTO> getById(@PathVariable Long id) {
        return service.getExamResultById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TÉCNICO DE LABORATORIO')")
    public ExamResultDTO create(@RequestBody ExamResultDTO dto) {
        return mapper.toDto(service.createExamResult(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TÉCNICO DE LABORATORIO')")
    public ExamResultDTO update(@RequestBody ExamResultDTO dto) {
        return mapper.toDto(service.updateExamResult(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeExamResultById(id);
    }
}
