package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    // La PK de ExamResult es String (id)
}
