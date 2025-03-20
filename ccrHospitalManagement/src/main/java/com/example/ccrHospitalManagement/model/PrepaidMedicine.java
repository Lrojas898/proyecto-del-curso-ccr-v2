package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PREPAID_MEDICINE")
@Data
public class PrepaidMedicine {
    @Id
    @Column(length = 30)
    private String id;

    @Column(length = 40)
    private String name;
}
