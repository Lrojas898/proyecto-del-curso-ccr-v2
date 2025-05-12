package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamResult;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ExamResultService {

    ExamResult createExamResult(ExamResult result, Authentication auth);

    ExamResult updateExamResult(ExamResult result, Authentication auth);

    List<ExamResult> getAllExamResults();

    Optional<ExamResult> getExamResultById(Long id);

    void removeExamResultById(Long id);
}
