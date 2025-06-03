package com.example.ccrHospitalManagement.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExamResultDTO {
    private Long id;
    private LocalDate resultDate;
    private String description;
    private Long examTypeId;
    private String examTypeName; // nuevo
    private String patientId;
    private String patientFirstName; // nuevo
    private String patientLastName;  // nuevo
    private String technicianId;
    private List<ExamResultDetailDTO> results;
}
