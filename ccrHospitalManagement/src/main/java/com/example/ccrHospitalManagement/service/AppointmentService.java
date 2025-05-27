package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.dto.RescheduleRequest;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.AppointmentStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);

    List<Appointment> getAllAppointments();

    Optional<Appointment> getAppointmentById(Long id);

    Appointment UpdateAppointment(Appointment appointment);

    void removeAppointmentById(Long id);

    Appointment updateAppointmentStatus(Long id, AppointmentStatus newStatus, String requesterRole);

    List<Appointment> getAppointmentsByPatientId(String patientId);
     Appointment handleRescheduleRequest(Long appointmentId, RescheduleRequest request, String username);

     long countAllAppointments();

}


