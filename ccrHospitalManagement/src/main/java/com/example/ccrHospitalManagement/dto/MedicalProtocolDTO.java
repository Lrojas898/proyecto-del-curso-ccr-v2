package com.example.ccrHospitalManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProtocolDTO {
    private Long id;
    private String name;
    private String description;
    private List<ProtocolTaskDTO> tasks = new ArrayList<>();
}
