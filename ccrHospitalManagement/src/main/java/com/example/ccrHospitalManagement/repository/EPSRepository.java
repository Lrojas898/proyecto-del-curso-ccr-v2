package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.EPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EPSRepository extends JpaRepository<EPS, String> {
    // La PK de EPS es String (nit)
}
