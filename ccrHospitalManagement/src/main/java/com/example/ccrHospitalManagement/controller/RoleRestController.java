package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.mapper.RoleMapper;
import com.example.ccrHospitalManagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleRestController {

    private final RoleService service;
    private final RoleMapper mapper;

    @GetMapping
    public List<RoleDTO> getAll() {
        return service.getAllRoles().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return service.getRoleById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RoleDTO create(@RequestBody RoleDTO dto) {
        return mapper.toDto(service.createRole(mapper.toEntity(dto)));
    }

    @PutMapping
    public RoleDTO update(@RequestBody RoleDTO dto) {
        return mapper.toDto(service.updateRole(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRole(id);
    }
}
