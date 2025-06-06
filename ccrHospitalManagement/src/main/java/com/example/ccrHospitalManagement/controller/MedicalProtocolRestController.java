package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.MedicalProtocolDTO;
import com.example.ccrHospitalManagement.dto.ProtocolTaskDTO;
import com.example.ccrHospitalManagement.mapper.MedicalProtocolMapper;
import com.example.ccrHospitalManagement.model.MedicalProtocol;
import com.example.ccrHospitalManagement.model.ProtocolTask;
import com.example.ccrHospitalManagement.service.MedicalProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/protocols")
@RequiredArgsConstructor
public class MedicalProtocolRestController {

    private final MedicalProtocolService medicalProtocolService;
    private final MedicalProtocolMapper medicalProtocolMapper;



    @GetMapping
    public ResponseEntity<List<MedicalProtocolDTO>> getAllProtocols() {
        List<MedicalProtocol> protocols = medicalProtocolService.getAllProtocols();
        return ResponseEntity.ok(medicalProtocolMapper.toDTOList(protocols));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalProtocolDTO> getProtocolById(@PathVariable Long id) {
        return medicalProtocolService.getProtocolById(id)
                .map(protocol -> ResponseEntity.ok(medicalProtocolMapper.toDTO(protocol)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicalProtocolDTO>> searchProtocols(@RequestParam String name) {
        List<MedicalProtocol> protocols = medicalProtocolService.searchProtocolsByName(name);
        return ResponseEntity.ok(medicalProtocolMapper.toDTOList(protocols));
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalProtocolDTO> createProtocol(@RequestBody MedicalProtocolDTO protocolDTO) {
        MedicalProtocol protocol = medicalProtocolMapper.toEntity(protocolDTO);
        MedicalProtocol savedProtocol = medicalProtocolService.saveProtocol(protocol);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalProtocolMapper.toDTO(savedProtocol));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalProtocolDTO> updateProtocol(
            @PathVariable Long id,
            @RequestBody MedicalProtocolDTO protocolDTO) {

        return medicalProtocolService.getProtocolById(id)
                .map(existingProtocol -> {
                    MedicalProtocol protocol = medicalProtocolMapper.toEntity(protocolDTO);
                    protocol.setId(id);
                    MedicalProtocol updatedProtocol = medicalProtocolService.saveProtocol(protocol);
                    return ResponseEntity.ok(medicalProtocolMapper.toDTO(updatedProtocol));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteProtocol(@PathVariable Long id) {
        if (!medicalProtocolService.getProtocolById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        medicalProtocolService.deleteProtocol(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{protocolId}/tasks")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ProtocolTaskDTO> addTaskToProtocol(
            @PathVariable Long protocolId,
            @RequestBody ProtocolTaskDTO taskDTO) {

        Optional<MedicalProtocol> protocolOpt = medicalProtocolService.getProtocolById(protocolId);
        if (!protocolOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MedicalProtocol protocol = protocolOpt.get();
        ProtocolTask task = medicalProtocolMapper.toTaskEntity(taskDTO, protocol);
        ProtocolTask savedTask = medicalProtocolService.addTaskToProtocol(protocolId, task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalProtocolMapper.toTaskDTO(savedTask));
    }

    @DeleteMapping("/{protocolId}/tasks/{taskId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteTaskFromProtocol(
            @PathVariable Long protocolId,
            @PathVariable Long taskId) {

        boolean deleted = medicalProtocolService.deleteTaskFromProtocol(protocolId, taskId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
