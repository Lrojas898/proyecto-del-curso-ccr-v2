package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.mapper.ClinicalHistoryMapper;
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
    public List<ClinicalHistoryDTO> getAll() {
        return service.getAllClinicalHistories().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<ClinicalHistoryDTO> getById(@PathVariable Long id, Authentication authentication) {
        return service.getClinicalHistoryById(id)
                .filter(history -> {
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
                        return history.getUser().getUsername().equals(authentication.getName());
                    }
                    return true;
                })
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ClinicalHistoryDTO create(@RequestBody ClinicalHistoryDTO dto) {
        return mapper.toDto(service.createClinicalHistory(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ClinicalHistoryDTO update(@RequestBody ClinicalHistoryDTO dto) {
        return mapper.toDto(service.UpdateClinicalHistory(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeClinicalHistoryById(id);
    }
}