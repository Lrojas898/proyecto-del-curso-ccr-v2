package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.mapper.AppointmentMapper;
import com.example.ccrHospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentService service;
    private final AppointmentMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public List<AppointmentDTO> getAll() {
        return service.getAllAppointments().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Long id) {
        return service.getAppointmentById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public AppointmentDTO create(@RequestBody AppointmentDTO dto) {
        return mapper.toDto(service.createAppointment(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public AppointmentDTO update(@RequestBody AppointmentDTO dto) {
        return mapper.toDto(service.UpdateAppointment(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public void delete(@PathVariable Long id) {
        service.removeAppointmentById(id);
    }
}