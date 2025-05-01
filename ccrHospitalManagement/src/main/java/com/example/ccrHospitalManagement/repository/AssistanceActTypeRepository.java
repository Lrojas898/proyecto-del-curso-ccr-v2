package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistanceActTypeRepository extends JpaRepository<AssistanceActType, Long> {
    // La PK de AssistanceActType es String (id)
}
