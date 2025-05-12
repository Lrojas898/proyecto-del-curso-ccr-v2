package com.example.ccrHospitalManagement.service;

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
}
