package com.example.ccrHospitalManagement.model;


import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLINICAL_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalHistory {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(nullable = false)
    private java.sql.Date date; // "date"

    @Column(name = "hour", nullable = false)
    private Date hour;

    @Column(name = "general_observations", nullable = false)
    private String generalObservations;

    @OneToOne
    @JoinColumn(name = "myuser_id", nullable = false)  // Relación 1 a 1 con User (como paciente)
    private User user;  // Un paciente tendrá una sola historia clínica
}
