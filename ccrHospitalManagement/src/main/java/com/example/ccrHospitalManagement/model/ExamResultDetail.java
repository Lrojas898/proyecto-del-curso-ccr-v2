package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EXAM_RESULT_DETAIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parameter;

    private String value;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "exam_result_id", nullable = false)
    private ExamResult examResult;
}
