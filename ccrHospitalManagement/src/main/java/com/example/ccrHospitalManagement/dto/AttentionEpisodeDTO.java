package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AttentionEpisodeDTO {
    private Long id;
    private LocalDate creationDate;
    private String description;
    private Long clinicalHistoryId;
    private UserDTO doctor;
    private Long appointmentId;
    private List<AssistanceActDTO> assistanceActs;

    private Long protocolId;
    private String protocolName;

    private List<DiagnosisDTO> diagnoses;

}
