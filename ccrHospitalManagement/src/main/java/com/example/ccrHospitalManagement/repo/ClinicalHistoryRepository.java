package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.ClinicalHistory;

public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, String> {
}
