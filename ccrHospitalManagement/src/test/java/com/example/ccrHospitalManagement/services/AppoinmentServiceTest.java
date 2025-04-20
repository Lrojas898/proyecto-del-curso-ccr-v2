package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.Location;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import com.example.ccrHospitalManagement.service.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
        doctor.setId("doc1");

        patient = new User();
        patient.setId("pat1");

        location = new Location();
        location.setId("loc1");
        location.setName("Main Office");
        location.setAddress("123 Main St");
        location.setDescription("Primary clinic");

        appointment = new Appointment();
        appointment.setId("app1");
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setLocation(location);
    }

    // CREATE
    @Test
    void createAppointment_Valid() {
        when(repository.existsById("app1")).thenReturn(false);
        when(repository.save(appointment)).thenReturn(appointment);
        Appointment result = service.createAppointment(appointment);
        assertNotNull(result);
        assertEquals("app1", result.getId());
        verify(repository).save(appointment);
    }

    @Test
    void createAppointment_DuplicateId_Throws() {
        when(repository.existsById("app1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("Ya existe una cita"));
    }

    @Test
    void createAppointment_NullId_Throws() {
        appointment.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("ID de la cita"));
    }

    @Test
    void createAppointment_PastDate_Throws() {
        appointment.setDate(LocalDate.now().minusDays(1));
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("fecha pasada"));
    }

    @Test
    void createAppointment_NullDate_Throws() {
        appointment.setDate(null);
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("fecha de la cita"));
    }

    @Test
    void createAppointment_NullTime_Throws() {
        appointment.setStartTime(null);
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora de inicio"));
    }

    @Test
    void createAppointment_InvalidTimeEarly_Throws() {
        appointment.setStartTime(LocalTime.of(7, 59));
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora debe estar"));
    }

    @Test
    void createAppointment_InvalidTimeLate_Throws() {
        appointment.setStartTime(LocalTime.of(18, 1));
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora debe estar"));
    }

    @Test
    void createAppointment_SameDoctorAndPatient_Throws() {
        appointment.getPatient().setId("doc1"); 
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("médico y el paciente"));
    }

    @Test
    void createAppointment_NullDoctor_Throws() {
        appointment.setDoctor(null);
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("paciente y un médico"));
    }

    @Test
    void createAppointment_NullPatient_Throws() {
        appointment.setPatient(null);
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("paciente y un médico"));
    }

    @Test
    void createAppointment_NullLocation_Throws() {
        appointment.setLocation(null);
        when(repository.existsById("app1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("especificarse una ubicación"));
    }

    @Test
    void createAppointment_BlankId_Throws() {
        appointment.setId("   "); 
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAppointment(appointment));
        assertTrue(e.getMessage().contains("ID de la cita"));
    }


    // UPDATE
    @Test
    void updateAppointment_Valid() {
        when(repository.existsById("app1")).thenReturn(true);
        when(repository.save(appointment)).thenReturn(appointment);
        Appointment updated = service.UpdateAppointment(appointment);
        assertNotNull(updated);
        assertEquals("app1", updated.getId());
    }

    @Test
    void updateAppointment_NotFound_Throws() {
        when(repository.existsById("app1")).thenReturn(false);
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
        when(repository.findById("app1")).thenReturn(Optional.of(appointment));
        Optional<Appointment> result = service.getAppointmentById("app1");
        assertTrue(result.isPresent());
    }

    @Test
    void getAppointmentById_NotFound() {
        when(repository.findById("nope")).thenReturn(Optional.empty());
        Optional<Appointment> result = service.getAppointmentById("nope");
        assertTrue(result.isEmpty());
    }

    // DELETE
    @Test
    void deleteAppointment_Valid() {
        when(repository.existsById("app1")).thenReturn(true);
        assertDoesNotThrow(() -> service.removeAppointmentById("app1"));
        verify(repository).deleteById("app1");
    }

    @Test
    void deleteAppointment_NotFound_Throws() {
        when(repository.existsById("NOT_FOUND")).thenReturn(false);
        
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                service.removeAppointmentById("NOT_FOUND"));
        assertTrue(e.getMessage().contains("No se puede eliminar una cita que no existe."));
        assertEquals("No se puede eliminar una cita que no existe.", e.getMessage());
    }
}



