package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ExamResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultDetailRepository extends JpaRepository<ExamResultDetail, Long> {
}
