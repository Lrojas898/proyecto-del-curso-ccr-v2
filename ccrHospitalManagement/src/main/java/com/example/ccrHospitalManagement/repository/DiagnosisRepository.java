package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {
    // La PK de Diagnosis es String (id)
}
