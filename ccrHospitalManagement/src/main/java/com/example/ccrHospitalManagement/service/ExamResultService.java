package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamResult;
import java.util.List;
import java.util.Optional;

public interface ExamResultService {

    ExamResult createExamResult(ExamResult examResult);
    List<ExamResult> getAllExamResults();
    Optional<ExamResult> getExamResultById(Long id);
    ExamResult updateExamResult(ExamResult result);
    void removeExamResultById(Long id);
}
