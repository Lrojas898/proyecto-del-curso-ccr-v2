package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.PrepaidMedicineDTO;
import com.example.ccrHospitalManagement.mapper.PrepaidMedicineMapper;
import com.example.ccrHospitalManagement.service.PrepaidMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prepaid-medicines")
@RequiredArgsConstructor
public class PrepaidMedicineRestController {

    private final PrepaidMedicineService service;
    private final PrepaidMedicineMapper mapper;

    @GetMapping
    public List<PrepaidMedicineDTO> getAll() {
        return service.getAllPrepaidMedicines().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrepaidMedicineDTO> getById(@PathVariable String id) {
        return service.getPrepaidMedicineById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PrepaidMedicineDTO create(@RequestBody PrepaidMedicineDTO dto) {
        return mapper.toDto(service.registerPrepaidMedicine(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PrepaidMedicineDTO update(@RequestBody PrepaidMedicineDTO dto) {
        return mapper.toDto(service.updatePrepaidMedicine(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        service.removePrepaidMedicineById(id);
    }
}
