package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryService {

    ClinicalHistory createClinicalHistory(ClinicalHistory history);
    List<ClinicalHistory> getAllClinicalHistories();

    ClinicalHistory UpdateClinicalHistory(ClinicalHistory history);

    @Transactional(readOnly = true)
    Optional<ClinicalHistory> getClinicalHistoryById(Long id);

    void removeClinicalHistoryById(Long id);

    Optional<ClinicalHistory> getClinicalHistoryByUserId(String userId);

    AttentionEpisode saveEpisode(AttentionEpisode episode);

    Optional<ClinicalHistory> getByUsername(String username);

    Optional<ClinicalHistory> getByUserId(String id);

    AssistanceAct addAssistanceActToEpisode(Long episodeId, AssistanceAct act);

    Optional<AttentionEpisode> getEpisodeById(Long id);

}
