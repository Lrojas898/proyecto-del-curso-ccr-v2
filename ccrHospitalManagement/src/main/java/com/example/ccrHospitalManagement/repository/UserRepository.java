package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // La PK de User es String (id)

    // Ejemplo de método para buscar por username:
    // Optional<User> findByUsername(String username);
}
