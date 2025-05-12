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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamResultServiceTest {

    @Mock
    private ExamResultRepository examResultRepository;

    @Mock
    private Authentication authTech;

    @Mock
    private Authentication authAdmin;

    @InjectMocks
    private ExamResultServiceImpl examResultService;

    private ExamResult result;

    @BeforeEach
    void setUp() {
        ExamType examType = new ExamType(1L, "Blood Test");

        User patient = new User();
        patient.setId("123456");

        User technician = new User();
        technician.setId("789012");

        result = new ExamResult();
        result.setId(1L);
        result.setResultDate(LocalDate.now());
        result.setDescription("Descripción válida del resultado.");
        result.setAttached("link/result.pdf");
        result.setExamType(examType);
        result.setPatient(patient);
        result.setTechnician(technician);

        // authTech con rol de técnico
        authTech = new Authentication() {
            @Override public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_LABTECH"));
            }
            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Object getPrincipal() { return null; }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
            @Override public String getName() { return "techUser"; }
        };

        // authAdmin con rol de admin
        authAdmin = new Authentication() {
            @Override public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Object getPrincipal() { return null; }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
            @Override public String getName() { return "adminUser"; }
        };
    }


    @Test
    void createExamResult_Valid() {
        when(examResultRepository.save(result)).thenReturn(result);
        ExamResult created = examResultService.createExamResult(result, authTech);
        assertNotNull(created);
        verify(examResultRepository).save(result);
    }

    @Test
    void createExamResult_FutureDate_Throws() {
        result.setResultDate(LocalDate.now().plusDays(1));
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("inválida o futura"));
    }

    @Test
    void createExamResult_ShortDescription_Throws() {
        result.setDescription("Corto");
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("al menos 10 caracteres"));
    }

    @Test
    void createExamResult_NullExamType_Throws() {
        result.setExamType(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("tipo de examen"));
    }

    @Test
    void createExamResult_NullPatientOrTech_Throws() {
        result.setPatient(null);
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e1.getMessage().contains("paciente como un técnico"));

        User patient = new User();
        patient.setId("54321");
        result.setPatient(patient);
        result.setTechnician(null);
        Exception e2 = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e2.getMessage().contains("paciente como un técnico"));
    }

    @Test
    void createExamResult_SamePatientAndTech_Throws() {
        User sameUser = new User();
        sameUser.setId("89412");
        result.setPatient(sameUser);
        result.setTechnician(sameUser);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("no pueden ser la misma"));
    }

    @Test
    void createExamResult_OnlySpacesDescription_Throws() {
        result.setDescription("     ");
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("descripción"));
    }

    @Test
    void createExamResult_EmptyDescription_Throws() {
        result.setDescription("");
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("descripción"));
    }

    @Test
    void createExamResult_NullResultDate_Throws() {
        result.setResultDate(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("fecha del resultado"));
    }

    @Test
    void createExamResult_NullDescription_Throws() {
        result.setDescription(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.createExamResult(result, authTech));
        assertTrue(e.getMessage().contains("descripción"));
    }

    @Test
    void updateExamResult_Valid() {
        when(examResultRepository.existsById(1L)).thenReturn(true);
        when(examResultRepository.save(result)).thenReturn(result);
        ExamResult updated = examResultService.updateExamResult(result, authAdmin);
        assertEquals(1L, updated.getId());
    }

    @Test
    void updateExamResult_NotFound_Throws() {
        when(examResultRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.updateExamResult(result, authAdmin));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateExamResult_ByTech_Throws() {
        when(examResultRepository.existsById(1L)).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.updateExamResult(result, authTech));
        assertTrue(e.getMessage().contains("administrador"));
    }

    @Test
    void getAllExamResults_ReturnsList() {
        when(examResultRepository.findAll()).thenReturn(List.of(result));
        List<ExamResult> list = examResultService.getAllExamResults();
        assertEquals(1, list.size());
    }

    @Test
    void getExamResultById_Found() {
        when(examResultRepository.findById(1L)).thenReturn(Optional.of(result));
        Optional<ExamResult> res = examResultService.getExamResultById(1L);
        assertTrue(res.isPresent());
    }

    @Test
    void getExamResultById_NotFound() {
        when(examResultRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<ExamResult> res = examResultService.getExamResultById(99L);
        assertTrue(res.isEmpty());
    }

    @Test
    void deleteExamResult_Valid() {
        when(examResultRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> examResultService.removeExamResultById(1L));
        verify(examResultRepository).deleteById(1L);
    }

    @Test
    void deleteExamResult_NotFound_Throws() {
        when(examResultRepository.existsById(99L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> examResultService.removeExamResultById(99L));
        assertEquals("No se puede eliminar un resultado que no existe.", e.getMessage());
    }
}
