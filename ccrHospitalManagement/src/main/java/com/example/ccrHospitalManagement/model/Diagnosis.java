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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;


    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;
}
