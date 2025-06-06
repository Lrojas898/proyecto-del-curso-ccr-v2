package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.dto.DiagnosisDTO;
import com.example.ccrHospitalManagement.mapper.DiagnosisMapper;
import com.example.ccrHospitalManagement.model.Diagnosis;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.DiagnosisRepository;
import com.example.ccrHospitalManagement.service.DiagnosisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiagnosisServiceTest {

    @Mock
    private DiagnosisRepository repository;

    @Mock
    private DiagnosisMapper mapper;

    @InjectMocks
    private DiagnosisServiceImpl service;

    private Diagnosis diagnosis;
    private DiagnosisDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("U001");
        user.setUsername("testuser");

        diagnosis = new Diagnosis();
        diagnosis.setId(1L);
        diagnosis.setName("Diagnosis 1");
        diagnosis.setDescription("Description");
        diagnosis.setCreatedBy(user);

        dto = new DiagnosisDTO();
        dto.setId(1L);
        dto.setName("Diagnosis 1");
        dto.setDescription("Description");
    }

    @Test
    void testFindAll() {
        List<Diagnosis> entities = List.of(diagnosis);
        List<DiagnosisDTO> dtos = List.of(dto);

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDTOList(entities)).thenReturn(dtos);

        List<DiagnosisDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Diagnosis 1", result.get(0).getName());
        verify(repository).findAll();
        verify(mapper).toDTOList(entities);
    }

    @Test
    void testCreateDiagnosis() {
        Diagnosis newDiagnosis = new Diagnosis();
        newDiagnosis.setName("Diagnosis 1");
        newDiagnosis.setDescription("Description");
        newDiagnosis.setCreatedBy(user);

        when(mapper.toEntity(dto)).thenReturn(newDiagnosis);
        when(repository.save(newDiagnosis)).thenReturn(diagnosis);
        when(mapper.toDto(diagnosis)).thenReturn(dto);

        DiagnosisDTO result = service.create(dto, user);

        assertEquals("Diagnosis 1", result.getName());
        verify(mapper).toEntity(dto);
        verify(repository).save(newDiagnosis);
        verify(mapper).toDto(diagnosis);
    }

    @Test
    void testUpdateDiagnosis_Success() {
        Diagnosis updated = new Diagnosis();
        updated.setId(1L);
        updated.setName("Updated Name");
        updated.setDescription("Updated Description");
        updated.setCreatedBy(user);

        DiagnosisDTO inputDto = new DiagnosisDTO();
        inputDto.setName("Updated Name");
        inputDto.setDescription("Updated Description");

        when(repository.findById(1L)).thenReturn(Optional.of(diagnosis));
        when(repository.save(any(Diagnosis.class))).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(inputDto);

        DiagnosisDTO result = service.update(1L, inputDto, user);

        assertEquals("Updated Name", result.getName());
        verify(repository).findById(1L);
        verify(repository).save(diagnosis);
        verify(mapper).toDto(updated);
    }

    @Test
    void testUpdateDiagnosis_NotFound_ThrowsException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> service.update(2L, dto, user));

        assertTrue(e.getMessage().contains("Diagnóstico no encontrado"));
    }

    @Test
    void testUpdateDiagnosis_NotAuthorized_ThrowsSecurityException() {
        User otherUser = new User();
        otherUser.setId("U002");
        diagnosis.setCreatedBy(otherUser);

        when(repository.findById(1L)).thenReturn(Optional.of(diagnosis));

        Exception e = assertThrows(SecurityException.class,
                () -> service.update(1L, dto, user));

        assertEquals("No autorizado para modificar este diagnóstico.", e.getMessage());
    }

    @Test
    void testSearchByName() {
        List<Diagnosis> entities = List.of(diagnosis);
        List<DiagnosisDTO> dtos = List.of(dto);

        when(repository.findByNameContainingIgnoreCase("Diag")).thenReturn(entities);
        when(mapper.toDTOList(entities)).thenReturn(dtos);

        List<DiagnosisDTO> result = service.searchByName("Diag");

        assertEquals(1, result.size());
        assertEquals("Diagnosis 1", result.get(0).getName());
    }
}
