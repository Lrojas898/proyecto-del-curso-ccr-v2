package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.AppointmentTasks;
import com.example.ccrHospitalManagement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentTasksRepository extends JpaRepository<AppointmentTasks, Long> {
    Optional<AppointmentTasks> findByAppointmentId(Long appointmentId);
}
