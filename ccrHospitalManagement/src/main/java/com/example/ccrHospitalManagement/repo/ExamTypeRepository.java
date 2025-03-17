package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.ExamType;

public interface ExamTypeRepository extends JpaRepository<ExamType, String> {
}
