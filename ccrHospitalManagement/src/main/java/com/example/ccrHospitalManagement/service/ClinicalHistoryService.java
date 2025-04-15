package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ClinicalHistory;
import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryService {

    ClinicalHistory createClinicalHistory(ClinicalHistory history);
    List<ClinicalHistory> getAllClinicalHistories();
    Optional<ClinicalHistory> getClinicalHistoryById(String id);
    ClinicalHistory UpdateClinicalHistory(ClinicalHistory history);
    void removeClinicalHistoryById(String id);
}
