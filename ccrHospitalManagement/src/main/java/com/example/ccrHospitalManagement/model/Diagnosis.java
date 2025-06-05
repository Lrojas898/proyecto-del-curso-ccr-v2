package com.example.ccrHospitalManagement.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "diagnoses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // Nombre del diagnóstico

    @Column(length = 1000)
    private String description;
}
