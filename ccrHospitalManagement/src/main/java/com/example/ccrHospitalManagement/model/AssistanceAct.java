package com.example.ccrHospitalManagement.model;



import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "ASSISTANCE_ACT")
@Data
public class AssistanceAct {
    @Id
    @Column(length = 50)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ASSISTANCE_ACT_TYPE_name")
    private AssistanceActType assistanceActType;

    @ManyToOne
    @JoinColumn(name = "ATTENTION_EPISODE_id")
    private AttentionEpisode attentionEpisode;

    @ManyToOne
    @JoinColumn(name = "STAFF_id")
    private Staff staff;
}
