package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.ExamType;
import com.example.ccrHospitalManagement.repository.ExamTypeRepository;
import com.example.ccrHospitalManagement.service.ExamTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamTypeServiceTest {

    @Mock
    private ExamTypeRepository examTypeRepository;

    @InjectMocks
    private ExamTypeServiceImpl examTypeService;

    private ExamType type1;
    private ExamType type2;

    @BeforeEach
    void setUp() {
        type1 = new ExamType("T1", "Hemograma");
        type2 = new ExamType("T2", "Radiografía de tórax");
    }

    // ---------- CREATE ----------
    @Test
    void createExamType_Valid() {
        when(examTypeRepository.existsById("T1")).thenReturn(false);
        when(examTypeRepository.save(type1)).thenReturn(type1);
        ExamType result = examTypeService.createExamType(type1);
        assertEquals("T1", result.getId());
        verify(examTypeRepository).save(type1);
    }

    @Test
    void createExamType_ExistingId_Throws() {
        when(examTypeRepository.existsById("T1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("Ya existe un tipo de examen"));
    }

    @Test
    void createExamType_NullId_Throws() {
        type1.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("ID del tipo de examen es obligatorio"));
    }

    @Test
    void createExamType_ShortName_Throws() {
        type1.setName("ab");
        when(examTypeRepository.existsById("T1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void createExamType_NullName_Throws() {
        type1.setName(null);
        when(examTypeRepository.existsById("T1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // ---------- UPDATE ----------
    @Test
    void updateExamType_Valid() {
        when(examTypeRepository.existsById("T2")).thenReturn(true);
        when(examTypeRepository.save(type2)).thenReturn(type2);
        ExamType result = examTypeService.updateExamType(type2);
        assertEquals("T2", result.getId());
        verify(examTypeRepository).save(type2);
    }

    @Test
    void updateExamType_NotFound_Throws() {
        when(examTypeRepository.existsById("T2")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.updateExamType(type2));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateExamType_InvalidName_Throws() {
        type2.setName("ab");
        when(examTypeRepository.existsById("T2")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.updateExamType(type2));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // ---------- GET ----------
    @Test
    void getAllExamTypes_ReturnsList() {
        when(examTypeRepository.findAll()).thenReturn(List.of(type1, type2));
        List<ExamType> result = examTypeService.getAllExamTypes();
        assertEquals(2, result.size());
    }

    @Test
    void getExamTypeById_Found() {
        when(examTypeRepository.findById("T1")).thenReturn(Optional.of(type1));
        Optional<ExamType> result = examTypeService.getExamTypeById("T1");
        assertTrue(result.isPresent());
    }

    @Test
    void getExamTypeById_NotFound() {
        when(examTypeRepository.findById("X")).thenReturn(Optional.empty());
        Optional<ExamType> result = examTypeService.getExamTypeById("X");
        assertTrue(result.isEmpty());
    }

    // ---------- DELETE ----------
    @Test
    void deleteExamType_Valid() {
        when(examTypeRepository.existsById("T1")).thenReturn(true);
        assertDoesNotThrow(() -> examTypeService.removeExamTypeById("T1"));
        verify(examTypeRepository).deleteById("T1");
    }

    @Test
    void deleteExamType_NotFound_Throws() {
        when(examTypeRepository.existsById("T999")).thenReturn(false);
    
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> examTypeService.removeExamTypeById("T999"));
    
        assertEquals("No se puede eliminar un tipo de examen que no existe.", exception.getMessage());
        verify(examTypeRepository, times(1)).existsById("T999");
        verify(examTypeRepository, never()).deleteById(anyString());
    }
    
}
