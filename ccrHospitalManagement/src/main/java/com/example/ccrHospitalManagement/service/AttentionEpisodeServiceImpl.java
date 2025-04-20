package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.repository.AttentionEpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttentionEpisodeServiceImpl implements AttentionEpisodeService {

    private final AttentionEpisodeRepository episodeRepository;

    @Override
    public AttentionEpisode createAttentionEpisode(AttentionEpisode episode) {
        validateEpisode(episode, true);
        return episodeRepository.save(episode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttentionEpisode> getAllAttentionEpisodes() {
        return episodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttentionEpisode> getAttentionEpisodeById(String id) {
        return episodeRepository.findById(id);
    }

    @Override
    public AttentionEpisode updateAttentionEpisode(AttentionEpisode episode) {
        if (!episodeRepository.existsById(episode.getId())) {
            throw new IllegalArgumentException("El episodio de atención no existe.");
        }
        validateEpisode(episode, false);
        return episodeRepository.save(episode);
    }

    @Override
    public void removeAttentionEpisodeById(String id) {
        if (!episodeRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un episodio que no existe.");
        }
        episodeRepository.deleteById(id);
    }

    private void validateEpisode(AttentionEpisode episode, boolean isCreate) {
        if (isCreate) {
            if (episode.getId() == null || episode.getId().isBlank()) {
                throw new IllegalArgumentException("El ID del episodio de atención es obligatorio.");
            }
            if (episodeRepository.existsById(episode.getId())) {
                throw new IllegalArgumentException("Ya existe un episodio de atención con ese ID.");
            }
        }

        if (episode.getCreationDate() == null) {
            throw new IllegalArgumentException("Debe establecerse la fecha de creación.");
        }

        if (episode.getCreationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede ser futura.");
        }

        if (episode.getDiagnosis() == null || episode.getDiagnosis().trim().length() < 10) {
            throw new IllegalArgumentException("El diagnóstico debe tener al menos 10 caracteres.");
        }

        if (episode.getDescription() == null || episode.getDescription().trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }

        if (episode.getClinicalHistory() == null) {
            throw new IllegalArgumentException("Debe asociarse a una historia clínica.");
        }

        if (episode.getDoctor() == null) {
            throw new IllegalArgumentException("Debe asignarse un médico responsable.");
        }
    }
}
