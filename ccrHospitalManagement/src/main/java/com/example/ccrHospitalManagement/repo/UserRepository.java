package com.example.ccrHospitalManagement.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
