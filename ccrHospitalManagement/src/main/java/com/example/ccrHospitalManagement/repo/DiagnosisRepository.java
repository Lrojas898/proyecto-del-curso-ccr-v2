package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Diagnosis;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {
}
