package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.AttentionEpisodeRepository;
import com.example.ccrHospitalManagement.service.AttentionEpisodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttentionEpisodeServiceTest {

    @Mock
    private AttentionEpisodeRepository repository;

    @InjectMocks
    private AttentionEpisodeServiceImpl service;

    private AttentionEpisode episode;

    @BeforeEach
    void setUp() {
        ClinicalHistory history = new ClinicalHistory();
        history.setId("HIST1");

        User doctor = new User();
        doctor.setId("DOC1");

        Appointment appointment = new Appointment();
        appointment.setId("APP1");

        episode = new AttentionEpisode();
        episode.setId("EP1");
        episode.setCreationDate(LocalDate.now().minusDays(1));
        episode.setDiagnosis("Diagnóstico válido");
        episode.setDescription("Descripción válida del episodio");
        episode.setClinicalHistory(history);
        episode.setDoctor(doctor);
        episode.setAppointment(appointment);
    }

    //CREATE

    @Test
    void createAttentionEpisode_Valid() {
        when(repository.existsById("EP1")).thenReturn(false);
        when(repository.save(episode)).thenReturn(episode);
        AttentionEpisode result = service.createAttentionEpisode(episode);
        assertNotNull(result);
        verify(repository).save(episode);
    }

    @Test
    void createAttentionEpisode_ExistingId_Throws() {
        when(repository.existsById("EP1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("Ya existe"));
    }

    @Test
    void createAttentionEpisode_NullId_Throws() {
        episode.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("ID del episodio"));
    }

    @Test
    void createAttentionEpisode_FutureDate_Throws() {
        episode.setCreationDate(LocalDate.now().plusDays(1));
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("no puede ser futura"));
    }

    @Test
    void createAttentionEpisode_ShortDiagnosis_Throws() {
        episode.setDiagnosis("Corto");
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createAttentionEpisode_ShortDescription_Throws() {
        episode.setDescription("Desc");
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createAttentionEpisode_NullDoctor_Throws() {
        episode.setDoctor(null);
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("asignarse un médico"));
    }

    @Test
    void createAttentionEpisode_NullHistory_Throws() {
        episode.setClinicalHistory(null);
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("historia clínica"));
    }

    @Test
    void createAttentionEpisode_NullDate_Throws() {
        episode.setCreationDate(null);
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("fecha de creación"));
    }

    @Test
    void createAttentionEpisode_NullDiagnosis_Throws() {
        episode.setDiagnosis(null);
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("diagnóstico"));
    }

    @Test
    void createAttentionEpisode_NullDescription_Throws() {
        episode.setDescription(null);
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("descripción"));
    }

    @Test
    void createAttentionEpisode_BlankId_Throws() {
        episode.setId("   ");  
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("ID del episodio"));
    }




    //UPDATE

    @Test
    void updateAttentionEpisode_Valid() {
        when(repository.existsById("EP1")).thenReturn(true);
        when(repository.save(episode)).thenReturn(episode);
        AttentionEpisode result = service.updateAttentionEpisode(episode);
        assertEquals("EP1", result.getId());
        verify(repository).save(episode);
    }

    @Test
    void updateAttentionEpisode_NotFound_Throws() {
        when(repository.existsById("EP1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.updateAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // GET

    @Test
    void getAllAttentionEpisodes_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(episode));
        List<AttentionEpisode> result = service.getAllAttentionEpisodes();
        assertEquals(1, result.size());
        assertEquals("EP1", result.get(0).getId());
    }

    @Test
    void getAttentionEpisodeById_Found() {
        when(repository.findById("EP1")).thenReturn(Optional.of(episode));
        Optional<AttentionEpisode> result = service.getAttentionEpisodeById("EP1");
        assertTrue(result.isPresent());
    }

    @Test
    void getAttentionEpisodeById_NotFound() {
        when(repository.findById("EPX")).thenReturn(Optional.empty());
        Optional<AttentionEpisode> result = service.getAttentionEpisodeById("EPX");
        assertTrue(result.isEmpty());
    }

    // Delete

    @Test
    void removeAttentionEpisodeById_Valid() {
        when(repository.existsById("EP1")).thenReturn(true);
        service.removeAttentionEpisodeById("EP1");
        verify(repository).deleteById("EP1");
    }

    @Test
    void removeAttentionEpisodeById_NotFound_Throws() {
        when(repository.existsById("EPX")).thenReturn(false);
    
        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> service.removeAttentionEpisodeById("EPX")
        );
    
        assertEquals("No se puede eliminar un episodio que no existe.", e.getMessage());
        verify(repository).existsById("EPX");
        verify(repository, never()).deleteById(any());
    }
    
}
