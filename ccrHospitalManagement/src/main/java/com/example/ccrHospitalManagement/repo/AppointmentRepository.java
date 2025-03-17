package com.example.ccrHospitalManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ccrHospitalManagement.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
