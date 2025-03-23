package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.UserRole;
import com.example.ccrHospitalManagement.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    // La PK compuesta de UserRole es UserRoleId
}
