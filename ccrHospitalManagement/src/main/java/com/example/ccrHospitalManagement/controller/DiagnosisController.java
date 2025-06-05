package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import com.example.ccrHospitalManagement.model.CustomUserDetails;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public List<DiagnosisDTO> findAll() {
        return diagnosisService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisDTO> create(@RequestBody DiagnosisDTO dto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        DiagnosisDTO created = diagnosisService.create(dto, user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisDTO> update(@PathVariable Long id,
                                               @RequestBody DiagnosisDTO dto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User user = userDetails.getUser();
            DiagnosisDTO updated = diagnosisService.update(id, dto, user);
            return ResponseEntity.ok(updated);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<DiagnosisDTO> search(@RequestParam String q) {
        return diagnosisService.searchByName(q);
    }
}
