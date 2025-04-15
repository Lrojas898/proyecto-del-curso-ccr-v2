package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(String id);
    Appointment UpdateAppointment(Appointment appointment);
    void removeAppointmentById(String id);
}
