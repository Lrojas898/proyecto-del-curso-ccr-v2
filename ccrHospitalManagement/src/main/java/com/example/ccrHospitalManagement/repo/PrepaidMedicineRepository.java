package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;

public interface PrepaidMedicineRepository extends JpaRepository<PrepaidMedicine, String> {
}

