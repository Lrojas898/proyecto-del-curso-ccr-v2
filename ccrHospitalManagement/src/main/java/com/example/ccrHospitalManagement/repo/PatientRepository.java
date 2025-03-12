package com.example.ccrHospitalManagement.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
