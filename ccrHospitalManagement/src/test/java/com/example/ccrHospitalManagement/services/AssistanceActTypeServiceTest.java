package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import com.example.ccrHospitalManagement.repository.AssistanceActTypeRepository;
import com.example.ccrHospitalManagement.service.AssistanceActTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssistanceActTypeServiceTest {

    @Mock
    private AssistanceActTypeRepository repository;

    @InjectMocks
    private AssistanceActTypeServiceImpl service;

    private AssistanceActType type;

    @BeforeEach
    void setUp() {
        type = new AssistanceActType();
        type.setId(1L); 
        type.setName("Consulta general");
    }

    // CREATE

    @Test
    void createAssistanceActType_Valid() {
        when(repository.save(type)).thenReturn(type);

        AssistanceActType result = service.createAssistanceActType(type);
        assertNotNull(result);
        verify(repository).save(type);
    }

    @Test
    void createAssistanceActType_ShortName_Throws() {
        type.setName("AB");

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAssistanceActType(type));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void createAssistanceActType_NullName_Throws() {
        type.setName(null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAssistanceActType(type));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // GET

    @Test
    void getAllAssistanceActTypes_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(type));

        List<AssistanceActType> result = service.getAllAssistanceActTypes();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getAllAssistanceActTypes_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<AssistanceActType> result = service.getAllAssistanceActTypes();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAssistanceActTypeById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(type));

        Optional<AssistanceActType> result = service.getAssistanceActTypeById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getAssistanceActTypeById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<AssistanceActType> result = service.getAssistanceActTypeById(99L);
        assertTrue(result.isEmpty());
    }

    // UPDATE

    @Test
    void updateAssistanceActType_Valid() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(type)).thenReturn(type);

        AssistanceActType result = service.UpdateAssistanceActType(type);
        assertEquals(1L, result.getId());
        verify(repository).save(type);
    }

    @Test
    void updateAssistanceActType_NotFound_Throws() {
        when(repository.existsById(1L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.UpdateAssistanceActType(type));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateAssistanceActType_ShortName_Throws() {
        type.setName("a");
        when(repository.existsById(1L)).thenReturn(true);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.UpdateAssistanceActType(type));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // DELETE

    @Test
    void deleteAssistanceActType_Valid() {
        when(repository.existsById(1L)).thenReturn(true);

        service.removeAssistanceActTypeById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteAssistanceActType_NotFound_Throws() {
        when(repository.existsById(99L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.removeAssistanceActTypeById(99L));

        assertEquals("No se puede eliminar un tipo que no existe.", e.getMessage());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}
