package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Appointment;
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
import com.example.ccrHospitalManagement.model.Location;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @InjectMocks private AppointmentServiceImpl appointmentService;

    private Appointment appointment;
    private User doctor;
    private User patient;

    @BeforeEach
    void setUp() {
        doctor = new User();
        doctor.setId("doc1");

        patient = new User();
        patient.setId("pat1");

        Location location = new Location();
        location.setId("loc1");
        location.setName("Consultorio 101");

        appointment = new Appointment();
        appointment.setId("appt1");
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setLocation(location); // ✅ Aquí usamos una instancia real de Location
    }


    @Test
    void createAppointment_Valid() {
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment saved = appointmentService.createAppointment(appointment);
        assertNotNull(saved);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void createAppointment_IdNull_Throws() {
        appointment.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("ID de la cita es obligatorio"));
    }

    @Test
    void createAppointment_DuplicateId_Throws() {
        when(appointmentRepository.existsById("appt1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("Ya existe una cita"));
    }

    @Test
    void createAppointment_DateInPast_Throws() {
        appointment.setDate(LocalDate.now().minusDays(1));
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("no puede agendarse"));
    }

    @Test
    void createAppointment_NullTime_Throws() {
        appointment.setStartTime(null);
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("hora de inicio"));
    }

    @Test
    void createAppointment_TimeOutOfRange_Throws() {
        appointment.setStartTime(LocalTime.of(7, 0));
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("entre las 08:00 y las 18:00"));
    }

    @Test
    void createAppointment_SameDoctorAndPatient_Throws() {
        patient.setId("doc1");
        appointment.setPatient(patient);
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("deben ser diferentes"));
    }

    @Test
    void createAppointment_MissingLocation_Throws() {
        appointment.setLocation(null);
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
        assertTrue(e.getMessage().contains("ubicación"));
    }

    @Test
    void updateAppointment_Valid() {
        when(appointmentRepository.existsById("appt1")).thenReturn(true);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        Appointment updated = appointmentService.UpdateAppointment(appointment);
        assertNotNull(updated);
    }

    @Test
    void updateAppointment_NotExist_Throws() {
        when(appointmentRepository.existsById("appt1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.UpdateAppointment(appointment));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateAppointment_DateInPast_Throws() {
        appointment.setDate(LocalDate.now().minusDays(1));
        when(appointmentRepository.existsById("appt1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> appointmentService.UpdateAppointment(appointment));
        assertTrue(e.getMessage().contains("no puede agendarse"));
    }

    @Test
    void removeAppointment_Valid() {
        when(appointmentRepository.existsById("appt1")).thenReturn(true);
        appointmentService.removeAppointmentById("appt1");
        verify(appointmentRepository).deleteById("appt1");
    }

    @Test
    void removeAppointment_NotFound_Throws() {
        when(appointmentRepository.existsById("apptX")).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
            appointmentService.removeAppointmentById("apptX")
        );

        assertEquals("No se puede eliminar una cita que no existe.", e.getMessage());
    }


    @Test
    void getAppointmentById_Found() {
        when(appointmentRepository.findById("appt1")).thenReturn(Optional.of(appointment));
        Optional<Appointment> result = appointmentService.getAppointmentById("appt1");
        assertTrue(result.isPresent());
    }

    @Test
    void getAppointmentById_NotFound() {
        when(appointmentRepository.findById("notexist")).thenReturn(Optional.empty());
        Optional<Appointment> result = appointmentService.getAppointmentById("notexist");
        assertFalse(result.isPresent());
    }

    @Test
    void getAllAppointments_ReturnsList() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        List<Appointment> result = appointmentService.getAllAppointments();
        assertEquals(1, result.size());
    }
}
