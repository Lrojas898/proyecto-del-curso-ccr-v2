package com.example.ccrHospitalManagement.model;


import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLINICAL_HISTORY",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"PATIENT_Id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalHistory {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(nullable = false)
    private java.sql.Date date; // "date"

    @Column(name = "\"hour\"", nullable = false)
    private Date hour;


    @Lob
    @Column(name = "general_observations", nullable = false)
    private String generalObservations;

    // 1:1 con Patient
    @OneToOne
    @JoinColumn(name = "PATIENT_Id", unique = true, nullable = false)
    private Patient patient;
}
