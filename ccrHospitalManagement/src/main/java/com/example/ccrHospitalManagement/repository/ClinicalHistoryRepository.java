package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ClinicalHistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, Long> {
    boolean existsByUserId(String userId);
    Optional<ClinicalHistory> findByUser_Username(String username);

    Optional<ClinicalHistory> findByUser_Id(String userId);

}
