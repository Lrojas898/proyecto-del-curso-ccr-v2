package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    // La PK de Appointment es String (id)
}
