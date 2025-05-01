package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.LocationDTO;
import com.example.ccrHospitalManagement.mapper.LocationMapper;
import com.example.ccrHospitalManagement.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationRestController {

    private final LocationService service;
    private final LocationMapper mapper;

    @GetMapping
    public List<LocationDTO> getAll() {
        return service.getAllLocations().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getById(@PathVariable Long id) {
        return service.getLocationById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LocationDTO create(@RequestBody LocationDTO dto) {
        return mapper.toDto(service.registerLocation(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LocationDTO update(@RequestBody LocationDTO dto) {
        return mapper.toDto(service.updateLocation(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeLocationById(id);
    }
}
