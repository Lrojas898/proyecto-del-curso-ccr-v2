package com.example.ccrHospitalManagement.model;


import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLINICAL_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ClinicalHistory {

    @Id
    @Column(length = 50, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @Column(nullable = false)
    @ToString.Include
    private java.sql.Date date; // "date"

    @Column(name = "general_observations", nullable = false)
    @ToString.Include
    private String generalObservations;

    @OneToOne
    @JoinColumn(name = "myuser_id", nullable = false)  // Relación 1 a 1 con User (como paciente)
    private User user;  // Un paciente tendrá una sola historia clínica
}
