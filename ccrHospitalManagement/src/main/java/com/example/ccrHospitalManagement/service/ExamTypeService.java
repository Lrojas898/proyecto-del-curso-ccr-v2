package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamType;
import java.util.List;
import java.util.Optional;

public interface ExamTypeService {

    ExamType createExamType(ExamType examType);
    List<ExamType> getAllExamTypes();
    Optional<ExamType> getExamTypeById(String id);
    ExamType updateExamType(ExamType examType);
    void removeExamTypeById(String id);
}
