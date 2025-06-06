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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.Map;

@Tag(name = "Appointment", description = "Operaciones relacionadas con citas médicas")
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentService service;
    private final AppointmentMapper mapper;

    @Operation(summary = "Obtener todas las citas médicas")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        return ResponseEntity.ok(service.getAllAppointments().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(@PathVariable String id) {
        return ResponseEntity.ok(service.getAppointmentsByPatientId(id).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/patient/{id}/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN','PACIENTE','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getCancelledAppointmentsByPatientId(@PathVariable String id) {
        return ResponseEntity.ok(service.getCancelledAppointmentsByPatientId(id).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctorId(@PathVariable String id) {
        return ResponseEntity.ok(service.getAppointmentsByDoctorId(id).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/doctor/{id}/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getCancelledAppointmentsByDoctorId(@PathVariable String id) {
        return ResponseEntity.ok(service.getCancelledAppointmentsByDoctorId(id).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getAllCancelledAppointments() {
        return ResponseEntity.ok(service.getAllCancelledAppointments().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/finalizable")
    @PreAuthorize("hasAnyRole('DOCTOR','ASISTENTE')")
    public ResponseEntity<List<AppointmentDTO>> getFinalizableAppointments() {
        return ResponseEntity.ok(service.getFinalizableAppointments().stream().map(mapper::toDto).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PACIENTE','ASISTENTE')")
    public ResponseEntity<?> create(@RequestBody AppointmentDTO dto) {
        try {
            Appointment appointment = service.createAppointment(mapper.toEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reschedule-request")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<?> requestReschedule(@PathVariable Long id,
                                               @RequestBody RescheduleRequest request,
                                               Authentication authentication) {
        try {
            String username = authentication.getName();
            Appointment updated = service.handleRescheduleRequest(id, request, username);
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel-by-patient")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<?> cancelByPatient(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Appointment cancelled = service.cancelByPatient(id, username);
            return ResponseEntity.ok(mapper.toDto(cancelled));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel-by-staff")
    @PreAuthorize("hasAnyRole('DOCTOR','ASISTENTE')")
    public ResponseEntity<?> cancelByStaff(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String reason = body.getOrDefault("reason", "");
            Appointment cancelled = service.cancelByStaff(id, reason);
            return ResponseEntity.ok(mapper.toDto(cancelled));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/finalize")
    @PreAuthorize("hasAnyRole('DOCTOR','ASISTENTE')")
    public ResponseEntity<?> finalizeAppointment(@PathVariable Long id) {
        try {
            Appointment finalized = service.finalizeAppointment(id);
            return ResponseEntity.ok(mapper.toDto(finalized));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('PACIENTE','DOCTOR','ASISTENTE')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody Map<String, String> body,
                                          Authentication authentication) {
        try {
            AppointmentStatus status = AppointmentStatus.valueOf(body.get("status"));
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Appointment updated = service.updateAppointmentStatus(id, status, role);
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
