package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.LocationType;

public interface LocationTypeRepository extends JpaRepository<LocationType, String> {
}

