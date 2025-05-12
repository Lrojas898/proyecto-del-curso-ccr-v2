package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.ClinicalHistoryRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.service.ClinicalHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalHistoryServiceTest {

    @Mock private ClinicalHistoryRepository clinicalHistoryRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private ClinicalHistoryServiceImpl service;

    private ClinicalHistory history;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("123456");

        history = new ClinicalHistory();
        history.setId(10L);
        history.setDate(LocalDate.now());
        history.setHour(LocalTime.of(10, 0));
        history.setGeneralObservations("Observación válida");
        history.setUser(user);
    }

    // Create

    @Test
    void createClinicalHistory_Valid() {
        when(userRepository.existsById("123456")).thenReturn(true);
        when(clinicalHistoryRepository.existsByUserId("123456")).thenReturn(false);
        when(clinicalHistoryRepository.save(history)).thenReturn(history);

        ClinicalHistory result = service.createClinicalHistory(history);
        assertNotNull(result);
        verify(clinicalHistoryRepository).save(history);
    }

    @Test
    void createClinicalHistory_FutureDate_Throws() {
        history.setDate(LocalDate.now().plusDays(1));
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("fecha"));
    }

    @Test
    void createClinicalHistory_NullDate_Throws() {
        history.setDate(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("fecha"));
    }

    @Test
    void createClinicalHistory_NullHour_Throws() {
        history.setHour(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("hora"));
    }

    @Test
    void createClinicalHistory_ShortObservation_Throws() {
        history.setGeneralObservations("Corto");
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("observaciones"));
    }

    @Test
    void createClinicalHistory_NullObservation_Throws() {
        history.setGeneralObservations(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("observaciones"));
    }

    @Test
    void createClinicalHistory_OnlySpacesObservation_Throws() {
        history.setGeneralObservations("        ");
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("observaciones"));
    }

    @Test
    void createClinicalHistory_NullUser_Throws() {
        history.setUser(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("usuario"));
    }

    @Test
    void createClinicalHistory_UserNotExists_Throws() {
        when(userRepository.existsById("123456")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("usuario"));
    }

    @Test
    void createClinicalHistory_UserAlreadyHasHistory_Throws() {
        when(userRepository.existsById("123456")).thenReturn(true);
        when(clinicalHistoryRepository.existsByUserId("123456")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("ya tiene una historia clínica"));
    }

    // Update

    @Test
    void updateClinicalHistory_Valid() {
        when(clinicalHistoryRepository.existsById(10L)).thenReturn(true);
        when(userRepository.existsById("123456")).thenReturn(true);
        when(clinicalHistoryRepository.save(history)).thenReturn(history);

        ClinicalHistory result = service.UpdateClinicalHistory(history);
        assertNotNull(result);
    }

    @Test
    void updateClinicalHistory_NotExists_Throws() {
        when(clinicalHistoryRepository.existsById(10L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.UpdateClinicalHistory(history));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // Remove

    @Test
    void removeClinicalHistoryById_Valid() {
        when(clinicalHistoryRepository.existsById(10L)).thenReturn(true);

        service.removeClinicalHistoryById(10L);
        verify(clinicalHistoryRepository).deleteById(10L);
    }

    @Test
    void removeClinicalHistoryById_NotFound_Throws() {
        when(clinicalHistoryRepository.existsById(99L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.removeClinicalHistoryById(99L));
        assertEquals("No se puede eliminar una historia clínica que no existe.", e.getMessage());
    }

    // Get

    @Test
    void getClinicalHistoryById_Found() {
        when(clinicalHistoryRepository.findById(10L)).thenReturn(Optional.of(history));

        Optional<ClinicalHistory> result = service.getClinicalHistoryById(10L);
        assertTrue(result.isPresent());
    }

    @Test
    void getClinicalHistoryById_NotFound() {
        when(clinicalHistoryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ClinicalHistory> result = service.getClinicalHistoryById(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllClinicalHistories_ReturnsList() {
        when(clinicalHistoryRepository.findAll()).thenReturn(List.of(history));

        List<ClinicalHistory> result = service.getAllClinicalHistories();
        assertEquals(1, result.size());
    }

    @Test
    void getAllClinicalHistories_EmptyList() {
        when(clinicalHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<ClinicalHistory> result = service.getAllClinicalHistories();
        assertTrue(result.isEmpty());
    }
}
