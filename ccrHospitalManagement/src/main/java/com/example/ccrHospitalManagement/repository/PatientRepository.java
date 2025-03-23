package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    // La PK de Patient es String (id)
}
