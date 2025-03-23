package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepaidMedicineRepository extends JpaRepository<PrepaidMedicine, String> {
    // La PK de PrepaidMedicine es String (nit)
}
