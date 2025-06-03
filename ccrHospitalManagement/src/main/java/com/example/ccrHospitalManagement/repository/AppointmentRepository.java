package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.AppointmentStatus;
import com.example.ccrHospitalManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(String patientId);
    List<Appointment> findByDoctorId(String doctorId);
    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<User> findDistinctPatientsByDoctorId(@Param("doctorId") String doctorId);
    boolean existsByDoctorIdAndDateAndStartTime(String id, LocalDate date, LocalTime startTime);
    boolean existsByDoctorIdAndDateAndStartTimeAndStatus(String doctorId, LocalDate date, LocalTime time, AppointmentStatus status);



}
