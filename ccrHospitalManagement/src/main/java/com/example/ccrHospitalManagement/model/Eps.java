package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EPS")
@Data
public class Eps {
    @Id
    @Column(length = 30)
    private String id;

    @Column(length = 50, nullable = false)
    private String name;
}
