package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SPECIALITY")
@Data
public class Speciality {
    @Id
    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String academicInstitution;
}
