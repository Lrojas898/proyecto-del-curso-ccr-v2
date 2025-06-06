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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        history.setId(1L);

        User doctor = new User();
        doctor.setId("123456");

        Appointment appointment = new Appointment();
        appointment.setId(3L);

        episode = new AttentionEpisode();
        episode.setId(4L);
        episode.setCreationDate(LocalDate.now().minusDays(1));
      
        episode.setDescription("Descripción válida del episodio");
        episode.setClinicalHistory(history);
        episode.setDoctor(doctor);
        episode.setAppointment(appointment);
    }

    // CREATE

    @Test
    void createAttentionEpisode_Valid() {
        when(repository.save(episode)).thenReturn(episode);

        AttentionEpisode result = service.createAttentionEpisode(episode);
        assertNotNull(result);
        verify(repository).save(episode);
    }

    @Test
    void createAttentionEpisode_FutureDate_Throws() {
        episode.setCreationDate(LocalDate.now().plusDays(1));
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("no puede ser futura"));
    }

  

    @Test
    void createAttentionEpisode_ShortDescription_Throws() {
        episode.setDescription("Desc");
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createAttentionEpisode_NullDoctor_Throws() {
        episode.setDoctor(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("asignarse un médico"));
    }

    @Test
    void createAttentionEpisode_NullHistory_Throws() {
        episode.setClinicalHistory(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("historia clínica"));
    }

    @Test
    void createAttentionEpisode_NullDate_AssignsToday() {
        episode.setCreationDate(null);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    
        AttentionEpisode result = service.createAttentionEpisode(episode);
    
        assertNotNull(result.getCreationDate());
        assertEquals(LocalDate.now(), result.getCreationDate());
        verify(repository).save(any());
    }
    


    @Test
    void createAttentionEpisode_NullDescription_Throws() {
        episode.setDescription(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.createAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("descripción"));
    }

    // UPDATE

    @Test
    void updateAttentionEpisode_Valid() {
        when(repository.findById(4L)).thenReturn(Optional.of(episode));
        when(repository.save(episode)).thenReturn(episode);
    
        AttentionEpisode result = service.updateAttentionEpisode(episode);
        assertEquals(4L, result.getId());
        verify(repository).save(episode);
    }
    

    @Test
    void updateAttentionEpisode_NotFound_Throws() {
        when(repository.findById(4L)).thenReturn(Optional.empty());
    
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.updateAttentionEpisode(episode));
        assertTrue(e.getMessage().contains("no existe"));
    }
    

    // GET

    @Test
    void getAllAttentionEpisodes_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(episode));

        List<AttentionEpisode> result = service.getAllAttentionEpisodes();
        assertEquals(1, result.size());
        assertEquals(4L, result.get(0).getId());
    }

    @Test
    void getAttentionEpisodeById_Found() {
        when(repository.findById(4L)).thenReturn(Optional.of(episode));

        Optional<AttentionEpisode> result = service.getAttentionEpisodeById(4L);
        assertTrue(result.isPresent());
    }

    @Test
    void getAttentionEpisodeById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<AttentionEpisode> result = service.getAttentionEpisodeById(99L);
        assertTrue(result.isEmpty());
    }

    // DELETE

    @Test
    void removeAttentionEpisodeById_Valid() {
        when(repository.existsById(4L)).thenReturn(true);

        service.removeAttentionEpisodeById(4L);
        verify(repository).deleteById(4L);
    }

    @Test
    void removeAttentionEpisodeById_NotFound_Throws() {
        when(repository.existsById(99L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> service.removeAttentionEpisodeById(99L));

        assertEquals("No se puede eliminar un episodio que no existe.", e.getMessage());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(any());
    }
}
