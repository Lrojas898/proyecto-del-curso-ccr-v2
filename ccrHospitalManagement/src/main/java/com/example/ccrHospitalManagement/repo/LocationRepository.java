package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
}
