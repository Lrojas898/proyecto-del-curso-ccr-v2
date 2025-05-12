package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ExamTypeDTO;
import com.example.ccrHospitalManagement.mapper.ExamTypeMapper;
import com.example.ccrHospitalManagement.service.ExamTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Tipos de Examen", description = "Operaciones para gestionar los tipos de exámenes médicos")
@RestController
@RequestMapping("/api/exam-types")
@RequiredArgsConstructor
public class ExamTypeRestController {

    private final ExamTypeService service;
    private final ExamTypeMapper mapper;

    @Operation(summary = "Obtener todos los tipos de exámenes")
    @GetMapping
    public ResponseEntity<List<ExamTypeDTO>> getAll() {
        try {
            List<ExamTypeDTO> examTypes = service.getAllExamTypes().stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(examTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un tipo de examen por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExamTypeDTO> getById(@PathVariable Long id) {
        try {
            return service.getExamTypeById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear un nuevo tipo de examen")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExamTypeDTO> create(@RequestBody ExamTypeDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createExamType(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un tipo de examen")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExamTypeDTO> update(@RequestBody ExamTypeDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.updateExamType(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un tipo de examen por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeExamTypeById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
