package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.LocationDTO;
import com.example.ccrHospitalManagement.mapper.LocationMapper;
import com.example.ccrHospitalManagement.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Ubicaciones", description = "Operaciones CRUD para gestionar ubicaciones en el sistema")
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationRestController {

    private final LocationService service;
    private final LocationMapper mapper;

    @Operation(summary = "Obtener todas las ubicaciones")
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAll() {
        try {
            List<LocationDTO> list = service.getAllLocations().stream().map(mapper::toDto).toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una ubicación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getById(@PathVariable Long id) {
        try {
            return service.getLocationById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear una nueva ubicación")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocationDTO> create(@RequestBody LocationDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.registerLocation(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar una ubicación existente")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocationDTO> update(@RequestBody LocationDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.updateLocation(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar una ubicación por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeLocationById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
