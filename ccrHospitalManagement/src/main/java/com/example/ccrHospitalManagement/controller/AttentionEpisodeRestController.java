package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.mapper.AttentionEpisodeMapper;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.service.AttentionEpisodeService;
import com.example.ccrHospitalManagement.service.AttentionEpisodeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attention-episodes")
@RequiredArgsConstructor
public class AttentionEpisodeRestController {

    private final AttentionEpisodeService service;
    private final AttentionEpisodeMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public List<AttentionEpisodeDTO> getAll() {
        return service.getAllForCurrentUser().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PACIENTE')")
    public ResponseEntity<AttentionEpisodeDTO> getById(@PathVariable Long id) {
        return service.getByIdIfAuthorized(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AttentionEpisodeDTO> create(@RequestBody AttentionEpisodeDTO dto) {
        AttentionEpisode entity = mapper.toEntity(dto);
        AttentionEpisode saved = service.createAttentionEpisodeWithAssociations(entity, dto);
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AttentionEpisodeDTO> update(@RequestBody AttentionEpisodeDTO dto) {
        AttentionEpisode entity = mapper.toEntity(dto);
        AttentionEpisode updated = service.updateAttentionEpisodeWithAssociations(entity, dto);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.removeAttentionEpisodeById(id);
        return ResponseEntity.noContent().build();
    }
}


