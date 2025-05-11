package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ExamResultDTO;
import com.example.ccrHospitalManagement.mapper.ExamResultMapper;
import com.example.ccrHospitalManagement.mapper.ExamResultMapperDecorator;
import com.example.ccrHospitalManagement.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-results")
@RequiredArgsConstructor
public class ExamResultRestController {

    private final ExamResultService service;
    private final ExamResultMapperDecorator mapper;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LABTECH')")
    public List<ExamResultDTO> getAll() {
        return service.getAllExamResults().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LABTECH','PACIENTE')")
    public ResponseEntity<ExamResultDTO> getById(@PathVariable Long id, Authentication auth) {
        return service.getExamResultById(id)
                .filter(result -> {
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
                        return result.getPatient().getUsername().equals(auth.getName());
                    }
                    return true;
                })
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(403).build());
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LABTECH')")
    public ExamResultDTO create(@RequestBody ExamResultDTO dto, Authentication auth) {
        return mapper.toDto(service.createExamResult(mapper.toEntity(dto), auth));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ExamResultDTO update(@RequestBody ExamResultDTO dto, Authentication auth) {
        return mapper.toDto(service.updateExamResult(mapper.toEntity(dto), auth));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeExamResultById(id);
    }
}
