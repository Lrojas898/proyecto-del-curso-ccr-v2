package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

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

    @Lob
    @Column(nullable = false, columnDefinition = "text")
    private String description;

    // Relación con Patient
    @ManyToOne
    @JoinColumn(name = "PATIENT_Id", nullable = false)
    private Patient patient;

    // Relación con Staff
    @ManyToOne
    @JoinColumn(name = "STAFF_id", nullable = false)
    private Staff staff;

    // Relación con Location
    @ManyToOne
    @JoinColumn(name = "LOCATION_id", nullable = false)
    private Location location;
}
