package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PROFESSION")
@Data
public class Profession {
    @Id
    @Column(length = 50)
    private String name;
}
