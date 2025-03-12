package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, String> {
}
