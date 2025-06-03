package com.example.ccrHospitalManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTaskDTO {
    private Long id;
    private String name;
    private String description;
    private boolean required;
    private boolean completed;
    private LocalDateTime completedAt;
    private String completedBy;
    private String notes;
}
