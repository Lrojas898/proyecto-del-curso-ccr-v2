package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.mapper.AttentionEpisodeMapper;
import com.example.ccrHospitalManagement.service.AttentionEpisodeService;
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
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<AttentionEpisodeDTO> getAll() {
        return service.getAllAttentionEpisodes().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AttentionEpisodeDTO> getById(@PathVariable Long id) {
        return service.getAttentionEpisodeById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public AttentionEpisodeDTO create(@RequestBody AttentionEpisodeDTO dto) {
        return mapper.toDto(service.createAttentionEpisode(mapper.toEntity(dto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public AttentionEpisodeDTO update(@RequestBody AttentionEpisodeDTO dto) {
        return mapper.toDto(service.updateAttentionEpisode(mapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.removeAttentionEpisodeById(id);
    }
}