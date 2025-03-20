package com.example.ccrHospitalManagement.model;


import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "APPOINTMENT_TYPE")
@Data
public class AppointmentType {
    @Id
    @Column(length = 50)
    private String name;
}
