package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ATTENTION_EPISODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttentionEpisode {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(nullable = false)
    private java.sql.Date creationDate; // creation_date -> creationDate

    // muchos Episodios se relacionan con 1 ClinicalHistory (1:N)
    @ManyToOne
    @JoinColumn(name = "CLINICAL_HISTORY_id", nullable = false)
    private ClinicalHistory clinicalHistory;
}
