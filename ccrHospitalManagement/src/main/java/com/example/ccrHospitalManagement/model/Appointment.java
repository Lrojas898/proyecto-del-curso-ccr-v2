package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APPOINTMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime startTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "LOCATION_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "USER_id_PATIENT", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "USER_id_DOCTOR", nullable = false)
    private User doctor;
}

