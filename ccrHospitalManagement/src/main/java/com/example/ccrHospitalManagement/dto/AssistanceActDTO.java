package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class AssistanceActDTO {
    private Long id;
    private Date date;
    private String description;
    private Long attentionEpisodeId;
    private Long typeId;
}