package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActTypeDTO;
import com.example.ccrHospitalManagement.mapper.AssistanceActTypeMapper;
import com.example.ccrHospitalManagement.service.AssistanceActTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assistance-act-types")
@RequiredArgsConstructor
public class AssistanceActTypeRestController {

    private final AssistanceActTypeService service;
    private final AssistanceActTypeMapper mapper;

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
