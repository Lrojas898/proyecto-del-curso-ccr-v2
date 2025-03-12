package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DIAGNOSIS")
@Data
public class Diagnosis {
    @Id
    @Column(length = 50)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ATTENTION_EPISODE_id")
    private AttentionEpisode attentionEpisode;
}
