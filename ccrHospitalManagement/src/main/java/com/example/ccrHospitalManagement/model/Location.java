package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "LOCATION")
@Data
public class Location {
    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "LOCATION_TYPE_name")
    private LocationType locationType;
}
