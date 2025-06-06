package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.sql.Date;
import java.time.LocalDate;

@Data
public class AssistanceActDTO {
    private Long id;
    private LocalDate issueDate;
    private String description;
    private Long attentionEpisodeId;
    private Long typeId;
    private UserDTO doctor;
}