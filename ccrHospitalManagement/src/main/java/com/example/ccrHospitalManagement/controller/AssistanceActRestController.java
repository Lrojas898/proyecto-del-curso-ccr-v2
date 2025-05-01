package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActDTO;
import com.example.ccrHospitalManagement.mapper.AssistanceActMapper;
import com.example.ccrHospitalManagement.service.AssistanceActService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assistance-acts")
@RequiredArgsConstructor
public class AssistanceActRestController {

    private final AssistanceActService service;
    private final AssistanceActMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<AssistanceActDTO> getAll() {
        return service.getAllAssistanceActs().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AssistanceActDTO> getById(@PathVariable Long id) {
        return service.getAssistanceActById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public AssistanceActDTO create(@RequestBody AssistanceActDTO dto) {
        return mapper.toDto(service.createAssistanceAct(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public AssistanceActDTO update(@RequestBody AssistanceActDTO dto) {
        return mapper.toDto(service.UpdateAssistanceAct(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeAssistanceActById(id);
    }
}