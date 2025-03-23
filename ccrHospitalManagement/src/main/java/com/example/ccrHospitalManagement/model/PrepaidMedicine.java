package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PREPAID_MEDICINE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidMedicine {

    @Id
    @Column(length = 30, nullable = false)
    private String nit;

    @Column(length = 30, nullable = false)
    private String name;
}
