package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.repository.AttentionEpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttentionEpisodeServiceImpl implements AttentionEpisodeService {

    @Autowired
    private AttentionEpisodeRepository attentionEpisodeRepository;

    @Override
    public AttentionEpisode createAttentionEpisode(AttentionEpisode entity) {
        return attentionEpisodeRepository.save(entity);
    }
    @Override
    public List<AttentionEpisode> getAllAttentionEpisodes() {
        return attentionEpisodeRepository.findAll();
    }

    @Override
    public Optional<AttentionEpisode> getAttentionEpisodeById(String id) {
        return attentionEpisodeRepository.findById(id);
    }

    @Override
    public AttentionEpisode UpdateAttentionEpisode(AttentionEpisode episode) {
        return attentionEpisodeRepository.save(episode);
    }

    @Override
    public void removeAttentionEpisodeById(String id) {
        attentionEpisodeRepository.deleteById(id);
    }
}
