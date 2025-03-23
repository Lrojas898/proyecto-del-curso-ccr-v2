package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    // La PK de Role es String (id)
}
