package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.EPSDTO;
import com.example.ccrHospitalManagement.mapper.EpsMapper;
import com.example.ccrHospitalManagement.service.EpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "EPS", description = "Gestión de Entidades Promotoras de Salud")
@RestController
@RequestMapping("/api/eps")
@RequiredArgsConstructor
public class EPSRestController {

    private final EpsService service;
    private final EpsMapper mapper;

    @Operation(summary = "Obtener todas las EPS")
    @GetMapping
    public ResponseEntity<List<EPSDTO>> getAll() {
        try {
            List<EPSDTO> list = service.getAllEPS().stream().map(mapper::toDto).toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener EPS por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EPSDTO> getById(@PathVariable String id) {
        try {
            return service.getEPSById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear una nueva EPS")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EPSDTO> create(@RequestBody EPSDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.registerEPS(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Actualizar una EPS existente")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EPSDTO> update(@RequestBody EPSDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.UpdateEPS(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Eliminar una EPS por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.removeEPSById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
