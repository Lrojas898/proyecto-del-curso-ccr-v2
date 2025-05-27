package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.dto.RescheduleRequest;
import com.example.ccrHospitalManagement.mapper.AppointmentMapper;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.AppointmentStatus;
import com.example.ccrHospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentService service;
    private final AppointmentMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        try {
            List<AppointmentDTO> appointments = service.getAllAppointments()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Long id) {
        try {
            return service.getAppointmentById(id)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<AppointmentDTO> create(@RequestBody AppointmentDTO dto) {
        try {
            System.out.println("Creating appointment: " + dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.toDto(service.createAppointment(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','ASISTENTE')")
    public ResponseEntity<AppointmentDTO> update(@RequestBody AppointmentDTO dto) {
        try {
            return ResponseEntity.ok(mapper.toDto(service.UpdateAppointment(mapper.toEntity(dto))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.removeAppointmentById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/patient/{id}")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(@PathVariable String id) {
    try {
        List<AppointmentDTO> appointments = service
                .getAppointmentsByPatientId(id)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(appointments);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/{id}/reschedule-request")
@PreAuthorize("hasRole('PACIENTE')")
public ResponseEntity<AppointmentDTO> requestReschedule(
        @PathVariable Long id,
        @RequestBody RescheduleRequest request,
        Authentication auth) {
    try {
        Appointment updated = service.handleRescheduleRequest(id, request, auth.getName());
        return ResponseEntity.ok(mapper.toDto(updated));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/count")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> getTotalAppointmentsCount() {
    try {
        long count = service.countAllAppointments();
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error al obtener el conteo total de citas");
    }
}







}
