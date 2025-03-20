package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ASSISTANCE_ACT_TYPE")
@Data
public class AssistanceActType {
    @Id
    @Column(length = 50)
    private String name;
}
