package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.repository.ClinicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    @Autowired
    private ClinicalHistoryRepository clinicalHistoryRepository;

    @Override
    public ClinicalHistory createClinicalHistory(ClinicalHistory entity) {
        return clinicalHistoryRepository.save(entity);
    }

    @Override
    public List<ClinicalHistory> getAllClinicalHistories() {
        return clinicalHistoryRepository.findAll();
    }

    @Override
    public Optional<ClinicalHistory> getClinicalHistoryById(String id) {
        return clinicalHistoryRepository.findById(id);
    }

    @Override
    public ClinicalHistory UpdateClinicalHistory(ClinicalHistory history) {
        return clinicalHistoryRepository.save(history);
    }

    @Override
    public void removeClinicalHistoryById(String id) {
        clinicalHistoryRepository.deleteById(id);
    }
}
