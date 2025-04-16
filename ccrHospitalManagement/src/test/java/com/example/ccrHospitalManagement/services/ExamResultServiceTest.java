package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.ExamResultRepository;
import com.example.ccrHospitalManagement.service.ExamResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamResultServiceTest {

    @Mock
    private ExamResultRepository examResultRepository;

    @InjectMocks
    private ExamResultServiceImpl examResultService;

    private ExamResult result;

    @BeforeEach
    void setUp() {
        ExamType examType = new ExamType("TYPE1", "Blood Test");

        User patient = new User();
        patient.setId("PAT001");

        User technician = new User();
        technician.setId("TECH001");

        result = new ExamResult();
        result.setId("R001");
        result.setResultDate(LocalDate.now());
        result.setDescription("Descripción válida del resultado.");
        result.setAttached("link/result.pdf");
        result.setExamType(examType);
        result.setPatient(patient);
        result.setTechnician(technician);
    }

    // ---------- CREATE ----------
    @Test
    void createExamResult_Valid() {
        when(examResultRepository.existsById("R001")).thenReturn(false);
        when(examResultRepository.save(result)).thenReturn(result);

        ExamResult created = examResultService.createExamResult(result);
        assertNotNull(created);
        verify(examResultRepository).save(result);
    }

    @Test
    void createExamResult_DuplicateId_Throws() {
        when(examResultRepository.existsById("R001")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("Ya existe"));
    }

    @Test
    void createExamResult_NullId_Throws() {
        result.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("ID del resultado"));
    }

    @Test
    void createExamResult_FutureDate_Throws() {
        result.setResultDate(LocalDate.now().plusDays(1));
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("inválida o futura"));
    }

    @Test
    void createExamResult_ShortDescription_Throws() {
        result.setDescription("Corto");
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createExamResult_NullExamType_Throws() {
        result.setExamType(null);
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("tipo de examen"));
    }

    @Test
    void createExamResult_NullPatientOrTech_Throws() {
        result.setPatient(null);
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e1.getMessage().contains("paciente como un técnico"));

        User patient = new User(); patient.setId("P1");
        result.setPatient(patient);
        result.setTechnician(null);
        Exception e2 = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e2.getMessage().contains("paciente como un técnico"));
    }

    @Test
    void createExamResult_SamePatientAndTech_Throws() {
        User sameUser = new User(); sameUser.setId("SAME1");
        result.setPatient(sameUser);
        result.setTechnician(sameUser);
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result));
        assertTrue(e.getMessage().contains("no pueden ser la misma"));
    }

    // ---------- UPDATE ----------
    @Test
    void updateExamResult_Valid() {
        when(examResultRepository.existsById("R001")).thenReturn(true);
        when(examResultRepository.save(result)).thenReturn(result);
        ExamResult updated = examResultService.updateExamResult(result);
        assertEquals("R001", updated.getId());
    }

    @Test
    void updateExamResult_NotFound_Throws() {
        when(examResultRepository.existsById("R001")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.updateExamResult(result));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // ---------- GET ----------
    @Test
    void getAllExamResults_ReturnsList() {
        when(examResultRepository.findAll()).thenReturn(List.of(result));
        List<ExamResult> list = examResultService.getAllExamResults();
        assertEquals(1, list.size());
    }

    @Test
    void getExamResultById_Found() {
        when(examResultRepository.findById("R001")).thenReturn(Optional.of(result));
        Optional<ExamResult> res = examResultService.getExamResultById("R001");
        assertTrue(res.isPresent());
    }

    @Test
    void getExamResultById_NotFound() {
        when(examResultRepository.findById("X")).thenReturn(Optional.empty());
        Optional<ExamResult> res = examResultService.getExamResultById("X");
        assertTrue(res.isEmpty());
    }

    // ---------- DELETE ----------
    @Test
    void deleteExamResult_Valid() {
        when(examResultRepository.existsById("R001")).thenReturn(true);
        assertDoesNotThrow(() -> examResultService.removeExamResultById("R001"));
        verify(examResultRepository).deleteById("R001");
    }

    @Test
    void deleteExamResult_NotFound_Throws() {
        when(examResultRepository.existsById("X")).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> 
            examResultService.removeExamResultById("X"));

        assertEquals("No se puede eliminar un resultado que no existe.", e.getMessage());
    }

}
