package com.example.ccrHospitalManagement.repository;


import com.example.ccrHospitalManagement.model.MedicalProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalProtocolRepository extends JpaRepository<MedicalProtocol, Long> {
    List<MedicalProtocol> findByNameContainingIgnoreCase(String name);
}
