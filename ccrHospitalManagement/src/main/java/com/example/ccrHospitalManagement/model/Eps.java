package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EPS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EPS {

    @Id
    @Column(length = 30, nullable = false)
    private String nit;

    @Column(length = 30, nullable = false)
    private String name;

}
