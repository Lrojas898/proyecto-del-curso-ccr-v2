package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ExamResultDTO;
import com.example.ccrHospitalManagement.mapper.ExamResultMapperDecorator;
import com.example.ccrHospitalManagement.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ExamResultDTO>> getAll() {
        try {
            List<ExamResultDTO> results = service.getAllExamResults().stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LABTECH','PACIENTE')")
    public ResponseEntity<ExamResultDTO> getById(@PathVariable Long id, Authentication auth) {
        try {
            return service.getExamResultById(id)
                    .filter(result -> {
                        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
                            return result.getPatient().getUsername().equals(auth.getName());
                        }
                        return true;
                    })
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LABTECH')")
    public ResponseEntity<ExamResultDTO> create(@RequestBody ExamResultDTO dto, Authentication auth) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createExamResult(mapper.toEntity(dto), auth)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExamResultDTO> update(@RequestBody ExamResultDTO dto, Authentication auth) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.updateExamResult(mapper.toEntity(dto), auth)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeExamResultById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
@PreAuthorize("hasRole('PACIENTE')")
public ResponseEntity<List<ExamResultDTO>> getMyResults(Authentication auth) {
    try {
        List<ExamResultDTO> myResults = service.getAllExamResults().stream()
                .filter(result -> result.getPatient().getUsername().equals(auth.getName()))
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(myResults);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
