package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssistanceActTypeRepository extends JpaRepository<AssistanceActType, Long> {
    Optional<Object> findByName(String treatment);
    // La PK de AssistanceActType es String (id)
}
