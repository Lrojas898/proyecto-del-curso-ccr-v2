package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EXAM_TYPE")
@Data
public class ExamType {
    @Id
    @Column(length = 50)
    private String name;
}
