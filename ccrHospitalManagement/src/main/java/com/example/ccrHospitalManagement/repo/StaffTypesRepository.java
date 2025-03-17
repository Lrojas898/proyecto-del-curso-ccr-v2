package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.StaffTypes;

public interface StaffTypesRepository extends JpaRepository<StaffTypes, String> {
}
