package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ExamTypeDTO;
import com.example.ccrHospitalManagement.mapper.ExamTypeMapper;
import com.example.ccrHospitalManagement.service.ExamTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-types")
@RequiredArgsConstructor
public class ExamTypeRestController {

    private final ExamTypeService service;
    private final ExamTypeMapper mapper;

    @GetMapping
    public List<ExamTypeDTO> getAll() {
        return service.getAllExamTypes().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamTypeDTO> getById(@PathVariable Long id) {
        return service.getExamTypeById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ExamTypeDTO create(@RequestBody ExamTypeDTO dto) {
        return mapper.toDto(service.createExamType(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ExamTypeDTO update(@RequestBody ExamTypeDTO dto) {
        return mapper.toDto(service.updateExamType(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeExamTypeById(id);
    }
}
