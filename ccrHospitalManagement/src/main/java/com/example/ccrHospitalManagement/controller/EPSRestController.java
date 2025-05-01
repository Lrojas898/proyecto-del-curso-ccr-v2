package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.EPSDTO;
import com.example.ccrHospitalManagement.mapper.EpsMapper;
import com.example.ccrHospitalManagement.service.EpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eps")
@RequiredArgsConstructor
public class EPSRestController {

    private final EpsService service;
    private final EpsMapper mapper;

    @GetMapping
    public List<EPSDTO> getAll() {
        return service.getAllEPS().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EPSDTO> getById(@PathVariable String id) {
        return service.getEPSById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EPSDTO create(@RequestBody EPSDTO dto) {
        return mapper.toDto(service.registerEPS(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EPSDTO update(@RequestBody EPSDTO dto) {
        return mapper.toDto(service.UpdateEPS(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        service.removeEPSById(id);
    }
}
