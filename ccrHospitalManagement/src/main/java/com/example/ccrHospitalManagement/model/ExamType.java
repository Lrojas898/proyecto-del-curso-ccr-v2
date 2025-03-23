package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EXAM_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamType {

    @Id
    @Column(length = 40, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String name;
}

