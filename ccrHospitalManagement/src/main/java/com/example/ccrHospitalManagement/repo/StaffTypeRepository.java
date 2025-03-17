package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.StaffType;

public interface StaffTypeRepository extends JpaRepository<StaffType, String> {
}
