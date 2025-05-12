package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.mapper.ClinicalHistoryMapperDecorator;
import com.example.ccrHospitalManagement.service.ClinicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinical-histories")
@RequiredArgsConstructor
public class ClinicalHistoryRestController {

    private final ClinicalHistoryService service;
    private final ClinicalHistoryMapperDecorator mapper;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<ClinicalHistoryDTO> getById(@PathVariable Long id, Authentication authentication) {
        try {
            return service.getClinicalHistoryById(id)
                    .filter(history -> {
                        if (authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
                            return history.getUser().getUsername().equals(authentication.getName());
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
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ClinicalHistoryDTO> create(@RequestBody ClinicalHistoryDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createClinicalHistory(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ClinicalHistoryDTO> update(@RequestBody ClinicalHistoryDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.UpdateClinicalHistory(mapper.toEntity(dto))));
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
            service.removeClinicalHistoryById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
