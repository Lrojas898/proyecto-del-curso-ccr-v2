package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private String description;
    private Long locationId;
    private String patientId;
    private String doctorId;
}