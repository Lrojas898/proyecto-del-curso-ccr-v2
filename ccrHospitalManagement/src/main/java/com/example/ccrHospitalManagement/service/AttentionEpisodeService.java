package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public interface AttentionEpisodeService {
    AttentionEpisode createAttentionEpisode(AttentionEpisode episode);
    AttentionEpisode updateAttentionEpisode(AttentionEpisode episode);
    void removeAttentionEpisodeById(Long id);
    Optional<AttentionEpisode> getAttentionEpisodeById(Long id);
    List<AttentionEpisode> getAllAttentionEpisodes();

    // NUEVOS métodos para lógica extendida
    List<AttentionEpisode> getAllForCurrentUser();
    Optional<AttentionEpisode> getByIdIfAuthorized(Long id);

    AttentionEpisode createAttentionEpisodeWithAssociations(AttentionEpisode episode, AttentionEpisodeDTO dto);
    AttentionEpisode updateAttentionEpisodeWithAssociations(AttentionEpisode episode, AttentionEpisodeDTO dto);
    AttentionEpisodeDTO updateEpisode(Long episodeId, AttentionEpisodeDTO dto, Authentication auth);
}

