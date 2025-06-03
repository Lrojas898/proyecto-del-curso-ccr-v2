package com.example.ccrHospitalManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTasksDTO {
    private Long id;
    private Long appointmentId;
    private MedicalProtocolDTO protocol;
    private List<AppointmentTaskDTO> tasks = new ArrayList<>();
}
