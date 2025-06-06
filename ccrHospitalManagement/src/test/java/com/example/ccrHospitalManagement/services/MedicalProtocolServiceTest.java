package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.MedicalProtocol;
import com.example.ccrHospitalManagement.model.ProtocolTask;
import com.example.ccrHospitalManagement.repository.MedicalProtocolRepository;
import com.example.ccrHospitalManagement.repository.ProtocolTaskRepository;
import com.example.ccrHospitalManagement.service.MedicalProtocolServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalProtocolServiceTest {

    @Mock
    private MedicalProtocolRepository medicalProtocolRepository;

    @Mock
    private ProtocolTaskRepository protocolTaskRepository;

    @InjectMocks
    private MedicalProtocolServiceImpl service;

    private MedicalProtocol protocol;
    private ProtocolTask task;

    @BeforeEach
    void setUp() {
        protocol = new MedicalProtocol();
        protocol.setId(1L);
        protocol.setName("Protocolo 1");

        task = new ProtocolTask();
        task.setId(1L);
        task.setName("Tarea A");
        task.setProtocol(protocol);
    }

    @Test
    void testGetAllProtocols() {
        when(medicalProtocolRepository.findAll()).thenReturn(List.of(protocol));
        List<MedicalProtocol> result = service.getAllProtocols();
        assertEquals(1, result.size());
        assertEquals("Protocolo 1", result.get(0).getName());
    }

    @Test
    void testGetProtocolById_Found() {
        when(medicalProtocolRepository.findById(1L)).thenReturn(Optional.of(protocol));
        Optional<MedicalProtocol> result = service.getProtocolById(1L);
        assertTrue(result.isPresent());
        assertEquals("Protocolo 1", result.get().getName());
    }

    @Test
    void testGetProtocolById_NotFound() {
        when(medicalProtocolRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<MedicalProtocol> result = service.getProtocolById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSearchProtocolsByName() {
        when(medicalProtocolRepository.findByNameContainingIgnoreCase("protocolo")).thenReturn(List.of(protocol));
        List<MedicalProtocol> result = service.searchProtocolsByName("protocolo");
        assertEquals(1, result.size());
    }

    @Test
    void testSaveProtocol() {
        when(medicalProtocolRepository.save(protocol)).thenReturn(protocol);
        MedicalProtocol saved = service.saveProtocol(protocol);
        assertEquals("Protocolo 1", saved.getName());
    }

    @Test
    void testDeleteProtocol() {
        service.deleteProtocol(1L);
        verify(medicalProtocolRepository).deleteById(1L);
    }

    @Test
    void testAddTaskToProtocol_Success() {
        ProtocolTask newTask = new ProtocolTask();
        newTask.setName("Nueva tarea");

        when(medicalProtocolRepository.findById(1L)).thenReturn(Optional.of(protocol));
        when(protocolTaskRepository.save(any(ProtocolTask.class))).thenAnswer(inv -> {
            ProtocolTask t = inv.getArgument(0);
            t.setId(2L);
            return t;
        });

        ProtocolTask savedTask = service.addTaskToProtocol(1L, newTask);

        assertEquals(2L, savedTask.getId());
        assertEquals(protocol, savedTask.getProtocol());
    }

    @Test
    void testAddTaskToProtocol_NotFound_Throws() {
        when(medicalProtocolRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.addTaskToProtocol(99L, new ProtocolTask()));
        assertTrue(ex.getMessage().contains("Protocolo no encontrado"));
    }

    @Test
    void testDeleteTaskFromProtocol_Success() {
        ProtocolTask existing = new ProtocolTask();
        existing.setId(1L);
        existing.setProtocol(protocol);

        when(protocolTaskRepository.findById(1L)).thenReturn(Optional.of(existing));

        boolean result = service.deleteTaskFromProtocol(1L, 1L);

        assertTrue(result);
        verify(protocolTaskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTaskFromProtocol_Fail_DifferentProtocol() {
        MedicalProtocol otherProtocol = new MedicalProtocol();
        otherProtocol.setId(2L);

        ProtocolTask existing = new ProtocolTask();
        existing.setId(1L);
        existing.setProtocol(otherProtocol);

        when(protocolTaskRepository.findById(1L)).thenReturn(Optional.of(existing));

        boolean result = service.deleteTaskFromProtocol(1L, 1L);
        assertFalse(result);
        verify(protocolTaskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteTaskFromProtocol_NotFound() {
        when(protocolTaskRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = service.deleteTaskFromProtocol(1L, 99L);
        assertFalse(result);
    }
}
