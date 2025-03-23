package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ClinicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, String> {
    // La PK de ClinicalHistory es String (id)
}
