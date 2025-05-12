package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.PrepaidMedicineDTO;
import com.example.ccrHospitalManagement.mapper.PrepaidMedicineMapper;
import com.example.ccrHospitalManagement.service.PrepaidMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Medicina Prepagada", description = "CRUD para entidades de medicina prepagada")
@RestController
@RequestMapping("/api/prepaid-medicines")
@RequiredArgsConstructor
public class PrepaidMedicineRestController {

    private final PrepaidMedicineService service;
    private final PrepaidMedicineMapper mapper;

    @Operation(summary = "Obtener todas las medicinas prepagadas")
    @GetMapping
    public ResponseEntity<List<PrepaidMedicineDTO>> getAll() {
        try {
            return ResponseEntity.ok(service.getAllPrepaidMedicines()
                    .stream()
                    .map(mapper::toDto)
                    .toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener medicina prepagada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PrepaidMedicineDTO> getById(@PathVariable String id) {
        try {
            return service.getPrepaidMedicineById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear una nueva medicina prepagada")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrepaidMedicineDTO> create(@RequestBody PrepaidMedicineDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.registerPrepaidMedicine(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar una medicina prepagada")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrepaidMedicineDTO> update(@RequestBody PrepaidMedicineDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.updatePrepaidMedicine(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar una medicina prepagada por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.removePrepaidMedicineById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
