package com.example.ccrHospitalManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolTaskDTO {
    private Long id;
    private String name;
    private String description;
    private boolean required;
    private int orderIndex;
}
