package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamResult;
import com.example.ccrHospitalManagement.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultServiceImpl implements ExamResultService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Override
    public ExamResult createExamResult(ExamResult result) {
        return examResultRepository.save(result);
    }

    @Override
    public List<ExamResult> getAllExamResults() {
        return examResultRepository.findAll();
    }

    @Override
    public Optional<ExamResult> getExamResultById(String id) {
        return examResultRepository.findById(id);
    }

    @Override
    public ExamResult UpdateExamResult(ExamResult result) {
        return examResultRepository.save(result);
    }

    @Override
    public void removeExamResultById(String id) {
        examResultRepository.deleteById(id);
    }
}
