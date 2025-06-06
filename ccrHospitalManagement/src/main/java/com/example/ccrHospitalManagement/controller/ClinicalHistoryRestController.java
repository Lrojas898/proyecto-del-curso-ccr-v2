package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.mapper.ClinicalHistoryMapperDecorator;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.service.ClinicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Tag(name = "Historias Clínicas", description = "Operaciones relacionadas con historias clínicas")
@RestController
@RequestMapping("/api/clinical-histories")
@RequiredArgsConstructor
public class ClinicalHistoryRestController {

    private final ClinicalHistoryService service;
    private final ClinicalHistoryMapperDecorator mapper;

    @Operation(summary = "Obtener todas las historias clínicas")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<List<ClinicalHistoryDTO>> getAll() {
        try {
            List<ClinicalHistoryDTO> list = service.getAllClinicalHistories()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una historia clínica por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<ClinicalHistoryDTO> getById(@PathVariable Long id, Authentication auth) {
        try {
            return service.getClinicalHistoryById(id)
                    .filter(history -> {
                        // Si es PACIENTE, solo puede ver su propia historia
                        if (auth.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
                            return history.getUser().getUsername().equals(auth.getName());
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

    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<?> getMyHistory(Authentication auth) {
        try {
            String username = auth.getName();

            Optional<ClinicalHistory> optional = service.getByUsername(username);

            if (optional.isPresent()) {
                ClinicalHistoryDTO dto = mapper.toDto(optional.get());
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró historia clínica para el usuario autenticado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar la historia clínica del paciente");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ClinicalHistoryDTO> create(@RequestBody ClinicalHistoryDTO dto) {
        try {
            var created = service.createClinicalHistory(mapper.toEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Actualizar una historia clínica")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ClinicalHistoryDTO> update(@RequestBody ClinicalHistoryDTO dto) {
        try {
            var updated = service.UpdateClinicalHistory(mapper.toEntity(dto));
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Eliminar una historia clínica por ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeClinicalHistoryById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-patient/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<ClinicalHistoryDTO> getHistoryByPatientId(@PathVariable String id) {
        try {
            Optional<ClinicalHistory> optional = service.getByUserId(id);
            return optional.map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




}
