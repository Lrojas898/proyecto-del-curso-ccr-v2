package com.example.ccrHospitalManagement.service;


import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import com.example.ccrHospitalManagement.model.User;

import java.util.List;

public interface DiagnosisService {
    List<DiagnosisDTO> findAll();
    DiagnosisDTO create(DiagnosisDTO dto, User user);
    DiagnosisDTO update(Long id, DiagnosisDTO dto, User user);
    List<DiagnosisDTO> searchByName(String q);
}
