package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;


    @Override
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment UpdateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public void removeAppointmentById(String id) {
        appointmentRepository.deleteById(id);
    }
}
