package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.mapper.AttentionEpisodeMapper;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.service.AttentionEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@Tag(name = "AttentionEpisode", description = "Endpoints para gestionar episodios de atención médica")
@RestController
@RequestMapping("/api/attention-episodes")
@RequiredArgsConstructor
public class AttentionEpisodeRestController {

    private final AttentionEpisodeService service;
    private final AttentionEpisodeMapper mapper;

    @Operation(summary = "Obtener todos los episodios de atención médica")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<List<AttentionEpisodeDTO>> getAll() {
        try {
            List<AttentionEpisodeDTO> list = service.getAllForCurrentUser()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un episodio de atención médica por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<AttentionEpisodeDTO> getById(@PathVariable Long id) {
        try {
            return service.getByIdIfAuthorized(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear un nuevo episodio de atención médica")
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AttentionEpisodeDTO> create(@RequestBody AttentionEpisodeDTO dto) {
        try {
            AttentionEpisode entity = mapper.toEntity(dto);
            AttentionEpisode saved = service.createAttentionEpisodeWithAssociations(entity, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un episodio de atención médica")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AttentionEpisodeDTO> update(@RequestBody AttentionEpisodeDTO dto) {
        try {
            AttentionEpisode entity = mapper.toEntity(dto);
            AttentionEpisode updated = service.updateAttentionEpisodeWithAssociations(entity, dto);
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un episodio de atención médica por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeAttentionEpisodeById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
