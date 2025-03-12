package com.example.ccrHospitalManagement.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Eps;

public interface EpsRepository extends JpaRepository<Eps, String> {
}
