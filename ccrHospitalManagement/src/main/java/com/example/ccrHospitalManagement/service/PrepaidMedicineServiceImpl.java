package com.example.ccrHospitalManagement.service;


import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrepaidMedicineServiceImpl {
    private final PrepaidMedicineRepository repository;
    public List<PrepaidMedicine> getAllPrepaids() {
        return repository.findAll();
    }
}
