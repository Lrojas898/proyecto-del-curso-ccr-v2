package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "EXAM_RESULT")
@Data
public class ExamResult {
    @Id
    @Column(length = 50)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date resultDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String result;

    @ManyToOne
    @JoinColumn(name = "PATIENT_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "STAFF_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "EXAM_TYPE_name")
    private ExamType examType;
}
