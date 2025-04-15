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
        type.setId("type1");
        type.setName("Consulta");

        ClinicalHistory clinicalHistory = new ClinicalHistory();
        clinicalHistory.setId("history1");

        User doctor = new User();
        doctor.setId("doctor1");

        episode = new AttentionEpisode();
        episode.setId("ep1");
        episode.setCreationDate(LocalDate.now());
        episode.setDiagnosis("Diagnóstico");
        episode.setDescription("Descripción");
        episode.setDoctor(doctor);
        episode.setClinicalHistory(clinicalHistory);

        act = new AssistanceAct();
        act.setId("A1");
        act.setIssueDate(Date.valueOf(LocalDate.now()));  // fecha válida
        act.setDescription("Descripción válida de acto asistencial");
        act.setType(type);
        act.setAttentionEpisode(episode);
    }

    // -------------------- CREATE --------------------

    @Test
    void createAssistanceAct_Valid() {
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        when(assistanceActRepository.save(act)).thenReturn(act);

        AssistanceAct result = assistanceActService.createAssistanceAct(act);
        assertNotNull(result);
        verify(assistanceActRepository).save(act);
    }

    @Test
    void createAssistanceAct_ExistingId_Throws() {
        when(assistanceActRepository.existsById("A1")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("Ya existe un acto asistencial"));
    }

    @Test
    void createAssistanceAct_NullId_Throws() {
        act.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("obligatorio"));
    }

    @Test
    void createAssistanceAct_FutureDate_Throws() {
        act.setIssueDate(Date.valueOf(LocalDate.now().plusDays(1)));
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("no puede estar en el futuro"));
    }

    @Test
    void createAssistanceAct_ShortDescription_Throws() {
        act.setDescription("Corto");
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createAssistanceAct_NullType_Throws() {
        act.setType(null);
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("tipo de acto asistencial"));
    }

    @Test
    void createAssistanceAct_NullEpisode_Throws() {
        act.setAttentionEpisode(null);
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("vinculado a un episodio"));
    }

    @Test
    void createAssistanceAct_NullDate_Throws() {
        act.setIssueDate(null);
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.createAssistanceAct(act));
        assertTrue(e.getMessage().contains("fecha de emisión"));
    }

    // -------------------- UPDATE --------------------

    @Test
    void updateAssistanceAct_Valid() {
        when(assistanceActRepository.existsById("A1")).thenReturn(true);
        when(assistanceActRepository.save(act)).thenReturn(act);
        AssistanceAct result = assistanceActService.UpdateAssistanceAct(act);
        assertEquals("A1", result.getId());
        verify(assistanceActRepository).save(act);
    }

    @Test
    void updateAssistanceAct_NotFound_Throws() {
        when(assistanceActRepository.existsById("A1")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> assistanceActService.UpdateAssistanceAct(act));
        assertTrue(e.getMessage().contains("no existe"));
    }

    // -------------------- DELETE --------------------

    @Test
    void removeAssistanceActById_Valid() {
        when(assistanceActRepository.existsById("A1")).thenReturn(true);
        assistanceActService.removeAssistanceActById("A1");
        verify(assistanceActRepository).deleteById("A1");
    }

    @Test
    void removeAssistanceActById_NotFound_Throws() {
        when(assistanceActRepository.existsById("NotFound")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                assistanceActService.removeAssistanceActById("NotFound"));
        assertTrue(e.getMessage().contains("No se puede eliminar un acto asistencial"));
    }


    // -------------------- GET --------------------

    @Test
    void getAssistanceActById_Found() {
        when(assistanceActRepository.findById("A1")).thenReturn(Optional.of(act));
        Optional<AssistanceAct> result = assistanceActService.getAssistanceActById("A1");
        assertTrue(result.isPresent());
        assertEquals("A1", result.get().getId());
    }

    @Test
    void getAssistanceActById_NotFound() {
        when(assistanceActRepository.findById("X")).thenReturn(Optional.empty());
        Optional<AssistanceAct> result = assistanceActService.getAssistanceActById("X");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllAssistanceActs_ReturnsList() {
        when(assistanceActRepository.findAll()).thenReturn(List.of(act));
        List<AssistanceAct> result = assistanceActService.getAllAssistanceActs();
        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getId());
    }

    @Test
    void getAllAssistanceActs_EmptyList() {
        when(assistanceActRepository.findAll()).thenReturn(Collections.emptyList());
        List<AssistanceAct> result = assistanceActService.getAllAssistanceActs();
        assertTrue(result.isEmpty());
    }
}
