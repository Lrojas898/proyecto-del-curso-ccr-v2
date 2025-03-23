package com.example.ccrHospitalManagement.model;



import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttentionEpisodeDiagnosisId implements Serializable {

    @Column(name = "ATTENTION_EPISODE_Id", length = 30)
    private String attentionEpisodeId;

    @Column(name = "DIAGNOSIS_id", length = 30)
    private String diagnosisId;
}
