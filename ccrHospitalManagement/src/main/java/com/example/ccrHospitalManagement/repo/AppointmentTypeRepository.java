package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.AppointmentType;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, String> {
}
