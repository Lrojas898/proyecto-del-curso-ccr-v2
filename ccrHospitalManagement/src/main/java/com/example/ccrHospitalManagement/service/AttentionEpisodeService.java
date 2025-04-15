package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AttentionEpisode;
import java.util.List;
import java.util.Optional;

public interface AttentionEpisodeService {

    AttentionEpisode createAttentionEpisode(AttentionEpisode episode);
    List<AttentionEpisode> getAllAttentionEpisodes();
    Optional<AttentionEpisode> getAttentionEpisodeById(String id);
    AttentionEpisode UpdateAttentionEpisode(AttentionEpisode episode);
    void removeAttentionEpisodeById(String id);
}
