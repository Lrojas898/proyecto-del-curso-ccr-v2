package com.example.ccrHospitalManagement.dto;

import com.example.ccrHospitalManagement.model.AppointmentStatus;
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
    private String locationName;

    private String patientId;
    private String patientFirstName;
    private String patientLastName;

    private String doctorId; 
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorSpeciality;

    private AppointmentStatus status;

    private UserDTO patient;
    private UserDTO doctor;
    private LocationDTO location;
}
