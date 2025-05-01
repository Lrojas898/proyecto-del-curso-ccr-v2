package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment, true);
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment UpdateAppointment(Appointment appointment) {
        if (!appointmentRepository.existsById(appointment.getId())) {
            throw new IllegalArgumentException("No se puede actualizar una cita que no existe.");
        }
        validateAppointment(appointment, false);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void removeAppointmentById(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una cita que no existe.");
        }
        appointmentRepository.deleteById(id);
    }

    private void validateAppointment(Appointment appointment, boolean isCreate) {

        if (appointment.getDate() == null) {
            throw new IllegalArgumentException("La fecha de la cita es obligatoria.");
        }
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La cita no puede agendarse en una fecha pasada.");
        }

        if (appointment.getStartTime() == null) {
            throw new IllegalArgumentException("La hora de inicio es obligatoria.");
        }
        if (appointment.getStartTime().isBefore(LocalTime.of(8, 0)) ||
            appointment.getStartTime().isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("La hora debe estar entre las 08:00 y las 18:00.");
        }

        if (appointment.getDoctor() == null || appointment.getPatient() == null) {
            throw new IllegalArgumentException("Debe asignarse un paciente y un médico.");
        }

        if (appointment.getDoctor().getId().equals(appointment.getPatient().getId())) {
            throw new IllegalArgumentException("El médico y el paciente deben ser diferentes.");
        }

        if (appointment.getLocation() == null) {
            throw new IllegalArgumentException("Debe especificarse una ubicación.");
        }
    }
}
