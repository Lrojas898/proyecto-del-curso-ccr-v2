package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActDTO;
import com.example.ccrHospitalManagement.mapper.AssistanceActMapper;
import com.example.ccrHospitalManagement.service.AssistanceActService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "AssistanceAct", description = "Operaciones CRUD para actos asistenciales")
@RestController
@RequestMapping("/api/assistance-acts")
@RequiredArgsConstructor
public class AssistanceActRestController {

    private final AssistanceActService service;
    private final AssistanceActMapper mapper;

    @Operation(summary = "Obtener todos los actos asistenciales")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<List<AssistanceActDTO>> getAll() {
        try {
            List<AssistanceActDTO> result = service.getAllAssistanceActs()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un acto asistencial por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AssistanceActDTO> getById(@PathVariable Long id) {
        try {
            return service.getAssistanceActById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear un nuevo acto asistencial")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AssistanceActDTO> create(@RequestBody AssistanceActDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createAssistanceAct(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un acto asistencial")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AssistanceActDTO> update(@RequestBody AssistanceActDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.UpdateAssistanceAct(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un acto asistencial por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeAssistanceActById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
