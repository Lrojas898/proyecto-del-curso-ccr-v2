package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "ATTENTION_EPISODE")
@Data
public class AttentionEpisode {
    @Id
    @Column(length = 30)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIME)
    private Date startHour;

    @Temporal(TemporalType.TIME)
    private Date finalHour;

    @Column(columnDefinition = "TEXT")
    private String generalObservations;

    @ManyToOne
    @JoinColumn(name = "CLINICAL_HISTORY_id")
    private ClinicalHistory clinicalHistory;

    @ManyToOne
    @JoinColumn(name = "APPOINTMENT_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "STAFF_id")
    private Staff staff;
}

