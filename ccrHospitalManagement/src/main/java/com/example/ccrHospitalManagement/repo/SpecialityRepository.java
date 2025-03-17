package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Speciality;

public interface SpecialityRepository extends JpaRepository<Speciality, String> {
}

