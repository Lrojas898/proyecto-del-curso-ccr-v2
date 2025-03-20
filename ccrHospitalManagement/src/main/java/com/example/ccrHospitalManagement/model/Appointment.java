package com.example.ccrHospitalManagement.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENT")
@Data
public class Appointment {
    @Id
    @Column(length = 50)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "STAFF_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "LOCATION_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "PATIENT_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "APPOINTMENT_TYPE_name")
    private AppointmentType appointmentType;
}

