package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ROLE")
@Data
public class Role {
    @Id
    @Column(length = 50)
    private String name;
}
