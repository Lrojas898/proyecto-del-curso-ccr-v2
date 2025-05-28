package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ATTENTION_EPISODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttentionEpisode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate creationDate;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "CLINICAL_HISTORY_id", nullable = false)
    private ClinicalHistory clinicalHistory;

    @ManyToOne
    @JoinColumn(name = "USER_DOCTOR_id", nullable = false)
    private User doctor;

    @OneToOne
    @JoinColumn(name = "APPOINTMENT_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "attentionEpisode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssistanceAct> assistanceActs;

}
