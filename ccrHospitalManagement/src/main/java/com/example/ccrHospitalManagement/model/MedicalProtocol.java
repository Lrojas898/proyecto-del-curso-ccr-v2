package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_protocols")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProtocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "protocol", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProtocolTask> tasks = new ArrayList<>();
}
