package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, String> {
}
