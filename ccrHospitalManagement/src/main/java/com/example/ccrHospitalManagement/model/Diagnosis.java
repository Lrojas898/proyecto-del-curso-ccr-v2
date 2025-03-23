package com.example.ccrHospitalManagement.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DIAGNOSIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;
}
