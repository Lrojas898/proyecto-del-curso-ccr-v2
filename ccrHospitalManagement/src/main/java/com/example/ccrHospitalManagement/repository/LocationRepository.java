package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    // La PK de Location es String (id)
}
