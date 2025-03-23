package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PATIENT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"USER_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(length = 5, nullable = false)
    private String bloodType; // blood_type -> bloodType

    private Double weight;
    private Double height;

    // Relación 1:1 con User
    @OneToOne
    @JoinColumn(name = "USER_id", nullable = false, unique = true)
    private User user;
}

