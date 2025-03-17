package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.AssistanceActType;

public interface AssistanceActTypeRepository extends JpaRepository<AssistanceActType, String> {
}
