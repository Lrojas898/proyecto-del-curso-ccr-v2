package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.Location;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import com.example.ccrHospitalManagement.service.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentServiceImpl service;

    private Appointment appointment;
    private User doctor;
    private User patient;
    private Location location;

    @BeforeEach
    void setUp() {
        doctor = new User();
        doctor.setId("123456");

        patient = new User();
        patient.setId("789012");

        location = new Location();
        location.setId(300L);
        location.setName("Main Office");
        location.setAddress("123 Main St");
        location.setDescription("Primary clinic");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setLocation(location);
    }

    // CREATE
    @Test
    void createAppointment_Valid() {
        when(repository.save(appointment)).thenReturn(appointment);
        Appointment result = service.createAppointment(appointment);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).save(appointment);
    }


    @Test
    void createAppointment_PastDate_Throws() {
        appointment.setDate(LocalDate.now().minusDays(1));
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("fecha pasada"));
    }

    @Test
    void createAppointment_NullDate_Throws() {
        appointment.setDate(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("fecha de la cita"));
    }

    @Test
    void createAppointment_NullTime_Throws() {
        appointment.setStartTime(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora de inicio"));
    }

    @Test
    void createAppointment_InvalidTimeEarly_Throws() {
        appointment.setStartTime(LocalTime.of(7, 59));
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora debe estar"));
    }

    @Test
    void createAppointment_InvalidTimeLate_Throws() {
        appointment.setStartTime(LocalTime.of(18, 1));
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora debe estar"));
    }

    @Test
    void createAppointment_SameDoctorAndPatient_Throws() {
        patient.setId("123456"); // mismo ID que doctor
        appointment.setPatient(patient);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("médico y el paciente"));
    }

    @Test
    void createAppointment_NullDoctor_Throws() {
        appointment.setDoctor(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("paciente y un médico"));
    }

    @Test
    void createAppointment_NullPatient_Throws() {
        appointment.setPatient(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("paciente y un médico"));
    }

    @Test
    void createAppointment_NullLocation_Throws() {
        appointment.setLocation(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("especificarse una ubicación"));
    }

    // UPDATE
    @Test
    void updateAppointment_Valid() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(appointment)).thenReturn(appointment);
        Appointment updated = service.UpdateAppointment(appointment);
        assertNotNull(updated);
        assertEquals(1L, updated.getId());
    }

    @Test
    void updateAppointment_NotFound_Throws() {
        when(repository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.UpdateAppointment(appointment));
        assertTrue(e.getMessage().contains("actualizar una cita que no existe"));
    }

    // GET
    @Test
    void getAllAppointments_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(appointment));
        List<Appointment> result = service.getAllAppointments();
        assertEquals(1, result.size());
    }

    @Test
    void getAppointmentById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        Optional<Appointment> result = service.getAppointmentById(1L);
        assertTrue(result.isPresent());
    }

    // DELETE
    @Test
    void deleteAppointment_Valid() {
        when(repository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> service.removeAppointmentById(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteAppointment_NotFound_Throws() {
        when(repository.existsById(99L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.removeAppointmentById(99L));
        assertTrue(e.getMessage().contains("No se puede eliminar una cita que no existe."));
    }
}
