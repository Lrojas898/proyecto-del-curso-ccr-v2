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
        user.setId("U1");

        history = new ClinicalHistory();
        history.setId("H1");
        history.setDate(LocalDate.now());
        history.setHour(LocalTime.of(10, 0));
        history.setGeneralObservations("Observación válida");
        history.setUser(user);
    }

    // -------------------- CREATE --------------------

    @Test
    void createClinicalHistory_Valid() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        when(userRepository.existsById("U1")).thenReturn(true);
        when(clinicalHistoryRepository.existsByUserId("U1")).thenReturn(false);
        when(clinicalHistoryRepository.save(history)).thenReturn(history);

        ClinicalHistory result = service.createClinicalHistory(history);
        assertNotNull(result);
        verify(clinicalHistoryRepository).save(history);
    }

    @Test
    void createClinicalHistory_DuplicateId_Throws() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(true);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().toLowerCase().contains("ya existe"));
    }


    @Test
    void createClinicalHistory_NullId_Throws() {
        history.setId(null);
        when(clinicalHistoryRepository.existsById(null)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("ID"));
    }

    @Test
    void createClinicalHistory_FutureDate_Throws() {
        history.setDate(LocalDate.now().plusDays(1));
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("fecha"));
    }

    @Test
    void createClinicalHistory_NullDate_Throws() {
        history.setDate(null);
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("fecha"));
    }

    @Test
    void createClinicalHistory_NullHour_Throws() {
        history.setHour(null);
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("hora"));
    }

    @Test
    void createClinicalHistory_ShortObservation_Throws() {
        history.setGeneralObservations("Corto");
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("observaciones"));
    }

    @Test
    void createClinicalHistory_NullUser_Throws() {
        history.setUser(null);
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("usuario"));
    }

    @Test
    void createClinicalHistory_UserNotExists_Throws() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        when(userRepository.existsById("U1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("usuario"));
    }

    @Test
    void createClinicalHistory_UserAlreadyHasHistory_Throws() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        when(userRepository.existsById("U1")).thenReturn(true);
        when(clinicalHistoryRepository.existsByUserId("U1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createClinicalHistory(history));
        assertTrue(e.getMessage().contains("ya tiene una historia clínica"));
    }

    // -------------------- UPDATE --------------------

    @Test
    void updateClinicalHistory_Valid() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(true);
        when(userRepository.existsById("U1")).thenReturn(true);
        when(clinicalHistoryRepository.save(history)).thenReturn(history);
        ClinicalHistory result = service.UpdateClinicalHistory(history);
        assertNotNull(result);
    }

    @Test
    void updateClinicalHistory_NotExists_Throws() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.UpdateClinicalHistory(history));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // -------------------- DELETE --------------------

    @Test
    void removeClinicalHistoryById_Valid() {
        when(clinicalHistoryRepository.existsById("H1")).thenReturn(true);
        service.removeClinicalHistoryById("H1");
        verify(clinicalHistoryRepository).deleteById("H1");
    }

    @Test
    void removeClinicalHistoryById_NotFound_Throws() {
        when(clinicalHistoryRepository.existsById("H2")).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.removeClinicalHistoryById("H2"));
        assertTrue(e.getMessage().toLowerCase().contains("no se puede eliminar"));
    }


    // -------------------- QUERY --------------------

    @Test
    void getClinicalHistoryById_Found() {
        when(clinicalHistoryRepository.findById("H1")).thenReturn(Optional.of(history));
        Optional<ClinicalHistory> result = service.getClinicalHistoryById("H1");
        assertTrue(result.isPresent());
    }

    @Test
    void getClinicalHistoryById_NotFound() {
        when(clinicalHistoryRepository.findById("H1")).thenReturn(Optional.empty());
        Optional<ClinicalHistory> result = service.getClinicalHistoryById("H1");
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
