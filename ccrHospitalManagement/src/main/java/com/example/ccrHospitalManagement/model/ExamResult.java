package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EXAM_RESULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(nullable = false)
    private java.sql.Date resultDate; // result_date -> resultDate

    @Lob
    @Column(nullable = false)
    private String description;

    @Lob
    private String attached;

    // Relación con ExamType
    @ManyToOne
    @JoinColumn(name = "EXAM_TYPE_id", nullable = false)
    private ExamType examType;

    // Relación con Patient
    @ManyToOne
    @JoinColumn(name = "PATIENT_Id", nullable = false)
    private Patient patient;
}
