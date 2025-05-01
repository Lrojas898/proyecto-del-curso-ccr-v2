package com.example.ccrHospitalManagement.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ASSISTANCE_ACT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceAct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private java.sql.Date issueDate; // issue_date -> issueDate

    @Lob
    @Column(nullable = false, columnDefinition = "text")
    private String description;

    // Relación con AttentionEpisode
    @ManyToOne
    @JoinColumn(name = "ATTENTION_EPISODE_Id", nullable = false)
    private AttentionEpisode attentionEpisode;

    // Relación con AssistanceActType
    @ManyToOne
    @JoinColumn(name = "ASSISTANCE_ACT_TYPE_id", nullable = false)
    private AssistanceActType type;
}
