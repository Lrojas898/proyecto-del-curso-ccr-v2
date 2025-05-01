package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActTypeDTO;
import com.example.ccrHospitalManagement.mapper.AssistanceActTypeMapper;
import com.example.ccrHospitalManagement.service.AssistanceActTypeService;
import lombok.RequiredArgsConstructor;
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
    public List<AssistanceActTypeDTO> getAll() {
        return service.getAllAssistanceActTypes().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssistanceActTypeDTO> getById(@PathVariable Long id) {
        return service.getAssistanceActTypeById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AssistanceActTypeDTO create(@RequestBody AssistanceActTypeDTO dto) {
        return mapper.toDto(service.createAssistanceActType(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AssistanceActTypeDTO update(@RequestBody AssistanceActTypeDTO dto) {
        return mapper.toDto(service.UpdateAssistanceActType(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeAssistanceActTypeById(id);
    }
}