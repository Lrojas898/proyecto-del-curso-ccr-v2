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

import java.util.List;
import java.util.Optional;

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
        type1 = new ExamType(1L, "Hemograma");
        type2 = new ExamType(2L, "Radiografía de tórax");
    }

    // Create
    @Test
    void createExamType_Valid() {
        when(examTypeRepository.save(type1)).thenReturn(type1);

        ExamType result = examTypeService.createExamType(type1);
        assertEquals(1L, result.getId());
        verify(examTypeRepository).save(type1);
    }

    @Test
    void createExamType_ShortName_Throws() {
        type1.setName("ab");

        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void createExamType_NullName_Throws() {
        type1.setName(null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.createExamType(type1));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // Update
    @Test
    void updateExamType_Valid() {
        when(examTypeRepository.existsById(2L)).thenReturn(true);
        when(examTypeRepository.save(type2)).thenReturn(type2);

        ExamType result = examTypeService.updateExamType(type2);
        assertEquals(2L, result.getId());
        verify(examTypeRepository).save(type2);
    }

    @Test
    void updateExamType_NotFound_Throws() {
        when(examTypeRepository.existsById(2L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.updateExamType(type2));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateExamType_InvalidName_Throws() {
        type2.setName("ab");
        when(examTypeRepository.existsById(2L)).thenReturn(true);

        Exception e = assertThrows(IllegalArgumentException.class, () -> examTypeService.updateExamType(type2));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // Get
    @Test
    void getAllExamTypes_ReturnsList() {
        when(examTypeRepository.findAll()).thenReturn(List.of(type1, type2));

        List<ExamType> result = examTypeService.getAllExamTypes();
        assertEquals(2, result.size());
    }

    @Test
    void getExamTypeById_Found() {
        when(examTypeRepository.findById(1L)).thenReturn(Optional.of(type1));

        Optional<ExamType> result = examTypeService.getExamTypeById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getExamTypeById_NotFound() {
        when(examTypeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ExamType> result = examTypeService.getExamTypeById(99L);
        assertTrue(result.isEmpty());
    }

    // Delete
    @Test
    void deleteExamType_Valid() {
        when(examTypeRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> examTypeService.removeExamTypeById(1L));
        verify(examTypeRepository).deleteById(1L);
    }

    @Test
    void deleteExamType_NotFound_Throws() {
        when(examTypeRepository.existsById(99L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> examTypeService.removeExamTypeById(99L));

        assertEquals("No se puede eliminar un tipo de examen que no existe.", e.getMessage());
        verify(examTypeRepository, times(1)).existsById(99L);
        verify(examTypeRepository, never()).deleteById(anyLong());
    }
}
