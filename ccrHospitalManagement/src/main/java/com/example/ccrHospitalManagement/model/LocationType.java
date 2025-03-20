package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LOCATION_TYPE")
@Data
public class LocationType {
    @Id
    @Column(length = 30)
    private String name;
}
