package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, String> {
    // La PK de ExamType es String (id)
}
