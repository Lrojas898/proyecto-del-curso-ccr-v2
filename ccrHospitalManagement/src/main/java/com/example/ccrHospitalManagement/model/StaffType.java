package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STAFF_TYPE")
@Data
public class StaffType {
    @Id
    @Column(length = 100)
    private String name;
}
