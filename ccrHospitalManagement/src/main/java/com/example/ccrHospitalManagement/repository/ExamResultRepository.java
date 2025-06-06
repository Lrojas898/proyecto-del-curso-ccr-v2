package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ExamResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    List<ExamResult> findByPatient_Username(String username);
    
}
