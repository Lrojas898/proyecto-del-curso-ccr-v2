package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AppointmentTaskDTO;
import com.example.ccrHospitalManagement.dto.AppointmentTasksDTO;
import com.example.ccrHospitalManagement.mapper.AppointmentTasksMapper;
import com.example.ccrHospitalManagement.model.AppointmentTask;
import com.example.ccrHospitalManagement.model.AppointmentTasks;
import com.example.ccrHospitalManagement.service.AppointmentTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentTasksController {

    private final AppointmentTasksService appointmentTasksService;
    private final AppointmentTasksMapper appointmentTasksMapper;

    @Autowired
    public AppointmentTasksController(
            AppointmentTasksService appointmentTasksService,
            AppointmentTasksMapper appointmentTasksMapper) {
        this.appointmentTasksService = appointmentTasksService;
        this.appointmentTasksMapper = appointmentTasksMapper;
    }

    @GetMapping("/{appointmentId}/tasks")
    public ResponseEntity<AppointmentTasksDTO> getAppointmentTasks(@PathVariable Long appointmentId) {
        return appointmentTasksService.getTasksByAppointmentId(appointmentId)
                .map(tasks -> ResponseEntity.ok(appointmentTasksMapper.toDTO(tasks)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{appointmentId}/protocol/{protocolId}")
    public ResponseEntity<AppointmentTasksDTO> assignProtocolToAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long protocolId) {

        try {
            AppointmentTasks tasks = appointmentTasksService.assignProtocolToAppointment(appointmentId, protocolId);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentTasksMapper.toDTO(tasks));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{appointmentId}/tasks/{taskId}")
    public ResponseEntity<AppointmentTaskDTO> updateTaskStatus(
            @PathVariable Long appointmentId,
            @PathVariable Long taskId,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {

        try {
            boolean completed = (boolean) request.get("completed");
            String notes = (String) request.getOrDefault("notes", "");
            String username = authentication.getName();

            AppointmentTask task = appointmentTasksService.updateTaskStatus(appointmentId, taskId, completed, notes, username);
            return ResponseEntity.ok(appointmentTasksMapper.toTaskDTO(task));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{appointmentId}/protocol-compliance")
    public ResponseEntity<Map<String, Boolean>> checkProtocolCompliance(@PathVariable Long appointmentId) {
        try {
            boolean compliant = appointmentTasksService.verifyProtocolCompliance(appointmentId);
            return ResponseEntity.ok(Map.of("compliant", compliant));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
