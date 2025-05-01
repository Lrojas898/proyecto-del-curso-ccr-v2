package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistanceActRepository extends JpaRepository<AssistanceAct, Long> {
    // La PK de AssistanceAct es String (id)
}
