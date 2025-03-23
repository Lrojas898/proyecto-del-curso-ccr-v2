package com.example.ccrHospitalManagement.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ATTENTION_EPISODE_DIAGNOSIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttentionEpisodeDiagnosis {

    @EmbeddedId
    private AttentionEpisodeDiagnosisId id;

    @ManyToOne
    @MapsId("attentionEpisodeId")
    @JoinColumn(name = "ATTENTION_EPISODE_Id", nullable = false)
    private AttentionEpisode attentionEpisode;

    @ManyToOne
    @MapsId("diagnosisId")
    @JoinColumn(name = "DIAGNOSIS_id", nullable = false)
    private Diagnosis diagnosis;
}
