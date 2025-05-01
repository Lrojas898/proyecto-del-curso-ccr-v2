package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "EXAM_RESULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate resultDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String attached;

    @ManyToOne
    @JoinColumn(name = "EXAM_TYPE_id", nullable = false)
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "USER_id_PATIENT", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "USER_id_TECH", nullable = false)
    private User technician;
}
