package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "AVAILABILITY")
@Data
public class Availability {
    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 10)
    private String weekDay;

    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Column(length = 1)
    private char status;
}
