package com.example.ccrHospitalManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDTO {
    private Long id;
    private String name;
    private String description;
    private String createdById;
}
