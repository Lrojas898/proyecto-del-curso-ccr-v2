package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AttentionEpisodeDTO {
    private Long id;
    private LocalDate creationDate;
    private String diagnosis;
    private String description;
    private Long clinicalHistoryId;
    private String doctorId;
    private Long appointmentId;

    private List<AssistanceActDTO> assistanceActs;
}