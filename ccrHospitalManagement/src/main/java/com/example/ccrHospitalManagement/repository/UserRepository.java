package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT ur.id FROM User u JOIN u.roles ur WHERE u.id = :userId")
    Set<String> findRoleIdsByUserId(String userId);

}
