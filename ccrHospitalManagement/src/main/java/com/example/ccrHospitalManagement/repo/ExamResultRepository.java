package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.ExamResult;

public interface ExamResultRepository extends JpaRepository<ExamResult, String> {
}
