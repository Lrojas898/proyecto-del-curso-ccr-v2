package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APPOINTMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    private java.sql.Date date;

    private java.sql.Date startTime; // start_time -> startTime

    @Column(nullable = false)
    private String description;

    // Relación con Location
    @ManyToOne
    @JoinColumn(name = "LOCATION_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private User professional;

}
