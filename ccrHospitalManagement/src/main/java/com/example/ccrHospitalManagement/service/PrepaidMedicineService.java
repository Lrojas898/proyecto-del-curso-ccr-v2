package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import java.util.List;
import java.util.Optional;

public interface PrepaidMedicineService {
    
    List<PrepaidMedicine> getAllPrepaidMedicines();
    Optional<PrepaidMedicine> getPrepaidMedicineById(String id);
    PrepaidMedicine registerOrUpdatePrepaidMedicine(PrepaidMedicine entity);
    void removePrepaidMedicineById(String id);
}
