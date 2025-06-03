package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "EXAM_TYPE_id", nullable = false)
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "USER_id_PATIENT", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "USER_id_TECH", nullable = false)
    private User technician;

    @OneToMany(mappedBy = "examResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamResultDetail> results;
}
