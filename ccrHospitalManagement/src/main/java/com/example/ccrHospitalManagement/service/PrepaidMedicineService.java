package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import java.util.List;
import java.util.Optional;

public interface PrepaidMedicineService {
    PrepaidMedicine registerPrepaidMedicine(PrepaidMedicine medicine);
    PrepaidMedicine updatePrepaidMedicine(PrepaidMedicine medicine);
    List<PrepaidMedicine> getAllPrepaidMedicines();
    Optional<PrepaidMedicine> getPrepaidMedicineById(String id);
    void removePrepaidMedicineById(String id);
}

