package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    // La PK de Staff es String (id)
}
