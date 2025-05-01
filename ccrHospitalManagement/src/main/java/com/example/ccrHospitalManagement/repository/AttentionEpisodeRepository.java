package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.AttentionEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttentionEpisodeRepository extends JpaRepository<AttentionEpisode, Long> {
    // La PK de AttentionEpisode es String (id)
}
