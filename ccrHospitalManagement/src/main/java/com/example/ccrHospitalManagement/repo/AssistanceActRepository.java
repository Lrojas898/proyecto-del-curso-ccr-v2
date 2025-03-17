package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.AssistanceAct;

public interface AssistanceActRepository extends JpaRepository<AssistanceAct, String> {
}
