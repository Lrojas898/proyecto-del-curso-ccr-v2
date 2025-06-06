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
    private String examTypeName;
    private String patientId;
    private String patientFirstName; 
    private String patientLastName;  
    private String technicianId;
    private List<ExamResultDetailDTO> results;
}
