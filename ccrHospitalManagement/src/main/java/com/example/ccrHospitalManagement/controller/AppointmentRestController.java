package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.model.AppointmentStatus;
import com.example.ccrHospitalManagement.service.AppointmentService;
import com.example.ccrHospitalManagement.mapper.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentService service;
    private final AppointmentMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public List<AppointmentDTO> getAll() {
        return service.getAllAppointments()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Long id) {
        return service.getAppointmentById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public AppointmentDTO create(@RequestBody AppointmentDTO dto) {
        System.out.println("Creating appointment: " + dto);
        return mapper.toDto(service.createAppointment(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','ASISTENTE')")
    public AppointmentDTO update(@RequestBody AppointmentDTO dto) {
        return mapper.toDto(service.UpdateAppointment(mapper.toEntity(dto)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','ASISTENTE','PACIENTE')")
    public ResponseEntity<AppointmentDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus newStatus,
            Authentication authentication) {

        
        String requesterRole = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("UNKNOWN");

        try {
            return ResponseEntity.ok(
                    mapper.toDto(service.updateAppointmentStatus(id, newStatus, requesterRole))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeAppointmentById(id);
    }
}
