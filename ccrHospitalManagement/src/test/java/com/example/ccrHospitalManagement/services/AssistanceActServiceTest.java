package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.AssistanceActRepository;
import com.example.ccrHospitalManagement.service.AssistanceActServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistanceActServiceTest {

    @Mock
    private AssistanceActRepository assistanceActRepository;

    @InjectMocks
    private AssistanceActServiceImpl assistanceActService;

    private AssistanceAct act;
    private AssistanceActType type;
    private AttentionEpisode episode;

    @BeforeEach
    void setUp() {
        type = new AssistanceActType();
        type.setId(10L); // Cambiado a Long
        type.setName("Consulta");

        ClinicalHistory clinicalHistory = new ClinicalHistory();
        clinicalHistory.setId(20L); // Cambiado a Long

        User doctor = new User();
        doctor.setId("123456"); // Cambiado a Long

        episode = new AttentionEpisode();
        episode.setId(40L); // Cambiado a Long
        episode.setCreationDate(LocalDate.now());
        episode.setDescription("Descripción válida");
        episode.setDoctor(doctor);
        episode.setClinicalHistory(clinicalHistory);

        act = new AssistanceAct();
        act.setId(1L); // Cambiado a Long
        act.setIssueDate(LocalDate.now());
        act.setDescription("Descripción válida de acto asistencial");
        act.setType(type);
        act.setAttentionEpisode(episode);
    }

    // CREATE

    @Test
    void createAssistanceAct_Valid() {
        when(assistanceActRepository.save(act)).thenReturn(act);

        AssistanceAct result = assistanceActService.createAssistanceAct(act);
        assertNotNull(result);
        verify(assistanceActRepository).save(act);
    }

    @Test
    void createAssistanceAct_FutureDate_Throws() {
        act.setIssueDate((LocalDate.now().plusDays(1)));
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("no puede estar en el futuro"));
    }

    @Test
    void createAssistanceAct_ShortDescription_Throws() {
        act.setDescription("Corto");
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createAssistanceAct_NullType_Throws() {
        act.setType(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("tipo de acto asistencial"));
    }

    @Test
    void createAssistanceAct_NullEpisode_Throws() {
        act.setAttentionEpisode(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("vinculado a un episodio"));
    }

    @Test
    void createAssistanceAct_NullDate_Throws() {
        act.setIssueDate(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("fecha de emisión"));
    }

    @Test
    void createAssistanceAct_NullDescription_Throws() {
        act.setDescription(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("descripción"));
    }

    // UPDATE

    @Test
    void updateAssistanceAct_Valid() {
        when(assistanceActRepository.existsById(1L)).thenReturn(true);
        when(assistanceActRepository.save(act)).thenReturn(act);

        AssistanceAct result = assistanceActService.UpdateAssistanceAct(act);
        assertEquals(1L, result.getId());
        verify(assistanceActRepository).save(act);
    }

    @Test
    void updateAssistanceAct_NotFound_Throws() {
        when(assistanceActRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.UpdateAssistanceAct(act));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // DELETE

    @Test
    void removeAssistanceActById_Valid() {
        when(assistanceActRepository.existsById(1L)).thenReturn(true);
        assistanceActService.removeAssistanceActById(1L);
        verify(assistanceActRepository).deleteById(1L);
    }

    @Test
    void removeAssistanceActById_NotFound_Throws() {
        when(assistanceActRepository.existsById(99L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> assistanceActService.removeAssistanceActById(99L));
        assertTrue(e.getMessage().contains("No se puede eliminar un acto asistencial"));
    }

    // GET

    @Test
    void getAssistanceActById_Found() {
        when(assistanceActRepository.findById(1L)).thenReturn(Optional.of(act));
        Optional<AssistanceAct> result = assistanceActService.getAssistanceActById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getAssistanceActById_NotFound() {
        when(assistanceActRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<AssistanceAct> result = assistanceActService.getAssistanceActById(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllAssistanceActs_ReturnsList() {
        when(assistanceActRepository.findAll()).thenReturn(List.of(act));
        List<AssistanceAct> result = assistanceActService.getAllAssistanceActs();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getAllAssistanceActs_EmptyList() {
        when(assistanceActRepository.findAll()).thenReturn(Collections.emptyList());
        List<AssistanceAct> result = assistanceActService.getAllAssistanceActs();
        assertTrue(result.isEmpty());
    }
}
