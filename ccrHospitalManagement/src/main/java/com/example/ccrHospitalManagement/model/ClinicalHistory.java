package com.example.ccrHospitalManagement.model;


import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLINICAL_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime hour;

    @Column(name = "general_observations", columnDefinition = "TEXT", nullable = false)
    private String generalObservations;

    @OneToOne
    @JoinColumn(name = "myuser_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttentionEpisode> attentionEpisodes = new ArrayList<>();

}

