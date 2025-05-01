package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ASSISTANCE_ACT_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceActType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    private String name;
}
