package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STAFF_TYPES")
@Data
public class StaffTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "STAFF_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "STAFF_TYPE_name", nullable = false)
    private StaffType staffType;
}
