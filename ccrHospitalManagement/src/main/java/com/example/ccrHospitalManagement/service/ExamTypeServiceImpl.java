package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamType;
import com.example.ccrHospitalManagement.repository.ExamTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamTypeServiceImpl implements ExamTypeService {

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Override
    public ExamType registerExamType(ExamType examType) {
        return examTypeRepository.save(examType);
    }

    @Override
    public List<ExamType> getAllExamTypes() {
        return examTypeRepository.findAll();
    }

    @Override
    public Optional<ExamType> getExamTypeById(String id) {
        return examTypeRepository.findById(id);
    }

    @Override
    public ExamType UpdateExamType(ExamType examType) {
        return examTypeRepository.save(examType);
    }

    @Override
    public void removeExamTypeById(String id) {
        examTypeRepository.deleteById(id);
    }
}
