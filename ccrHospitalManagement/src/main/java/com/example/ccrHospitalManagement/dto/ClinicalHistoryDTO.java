package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ClinicalHistoryDTO {
    private Long id;
    private LocalDate date;
    private LocalTime hour;
    private String generalObservations;
    private String userId;
    private List<AttentionEpisodeDTO> attentionEpisodes;

}