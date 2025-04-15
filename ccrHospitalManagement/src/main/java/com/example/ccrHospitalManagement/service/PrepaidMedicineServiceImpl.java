package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrepaidMedicineServiceImpl implements PrepaidMedicineService {

    @Autowired
    private PrepaidMedicineRepository prepaidMedicineRepository;

    @Override
    public List<PrepaidMedicine> getAllPrepaidMedicines() {
        return prepaidMedicineRepository.findAll();
    }

    @Override
    public Optional<PrepaidMedicine> getPrepaidMedicineById(String id) {
        return prepaidMedicineRepository.findById(id);
    }

    @Override
    public PrepaidMedicine registerOrUpdatePrepaidMedicine(PrepaidMedicine entity) {
        return prepaidMedicineRepository.save(entity);
    }

    @Override
    public void removePrepaidMedicineById(String id) {
        prepaidMedicineRepository.deleteById(id);
    }
}

