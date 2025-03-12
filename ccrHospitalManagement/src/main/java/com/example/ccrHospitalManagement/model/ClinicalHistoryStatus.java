package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CLINIC_HISTORIAL_STATUS")
@Data
public class ClinicalHistoryStatus {
    @Id
    @Column(length = 8)
    private String name;
}
