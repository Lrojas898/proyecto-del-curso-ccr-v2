package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AttentionEpisodeDTO {
    private Long id;
    private LocalDate creationDate;
    private String diagnosis;
    private String description;
    private Long clinicalHistoryId;
    private String doctorId;
    private Long appointmentId;
}