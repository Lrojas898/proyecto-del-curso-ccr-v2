package com.example.ccrHospitalManagement.model;


import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STAFF_SPECIALITIES")
@Data
public class StaffSpecialities {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "STAFF_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "SPECIALITY_name", nullable = false)
    private Speciality speciality;
}
