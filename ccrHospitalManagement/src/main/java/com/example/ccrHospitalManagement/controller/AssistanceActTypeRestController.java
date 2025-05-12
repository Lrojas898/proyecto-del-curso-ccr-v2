package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActTypeDTO;
import com.example.ccrHospitalManagement.mapper.AssistanceActTypeMapper;
import com.example.ccrHospitalManagement.service.AssistanceActTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "AssistanceActType", description = "Operaciones para tipos de actos asistenciales")
@RestController
@RequestMapping("/api/assistance-act-types")
@RequiredArgsConstructor
public class AssistanceActTypeRestController {

    private final AssistanceActTypeService service;
    private final AssistanceActTypeMapper mapper;

    @Operation(summary = "Obtener todos los tipos de actos asistenciales")
    @GetMapping
    public ResponseEntity<List<AssistanceActTypeDTO>> getAll() {
        try {
            List<AssistanceActTypeDTO> types = service.getAllAssistanceActTypes()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un tipo de acto asistencial por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AssistanceActTypeDTO> getById(@PathVariable Long id) {
        try {
            return service.getAssistanceActTypeById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear un nuevo tipo de acto asistencial")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssistanceActTypeDTO> create(@RequestBody AssistanceActTypeDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createAssistanceActType(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un tipo de acto asistencial")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssistanceActTypeDTO> update(@RequestBody AssistanceActTypeDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.UpdateAssistanceActType(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un tipo de acto asistencial por su ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeAssistanceActTypeById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
