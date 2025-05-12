package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExamResultDTO {
    private Long id;
    private LocalDate resultDate;
    private String description;
    private String attached;
    private Long examTypeId;
    private String patientId;
    private String technicianId;
}
