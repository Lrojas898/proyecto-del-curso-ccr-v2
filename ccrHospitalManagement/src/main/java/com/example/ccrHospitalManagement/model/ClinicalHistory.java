package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "CLINICAL_HISTORY")
@Data
public class ClinicalHistory {
    @Id
    @Column(length = 50)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "CLINIC_HISTORIAL_STATUS_name")
    private ClinicalHistoryStatus clinicalHistoryStatus;

    @ManyToOne
    @JoinColumn(name = "PATIENT_id")
    private Patient patient;
}
