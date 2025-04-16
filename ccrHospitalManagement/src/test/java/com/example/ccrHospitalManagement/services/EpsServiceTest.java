package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.repository.EPSRepository;
import com.example.ccrHospitalManagement.service.EpsServiceImpl;
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
public class EpsServiceTest {

    @Mock
    private EPSRepository epsRepository;

    @InjectMocks
    private EpsServiceImpl epsService;

    private EPS eps, eps2, eps3, eps4, eps5;
    private List<EPS> allEPS;

    @BeforeEach
    void setUp() {
        eps = new EPS("EPS123", "Salud Total");
        eps2 = new EPS("EPS124", "Nueva EPS");
        eps3 = new EPS("EPS125", "EPS Compensar");
        eps4 = new EPS("EPS126", "Sura");
        eps5 = new EPS("EPS127", "Sanitas");

        allEPS = List.of(eps, eps2, eps3, eps4, eps5);
    }

    // ---------- CREATE ----------
    @Test
    void registerEPS_Valid() {
        when(epsRepository.existsById("EPS123")).thenReturn(false);
        when(epsRepository.save(eps)).thenReturn(eps);

        EPS result = epsService.registerEPS(eps);
        assertNotNull(result);
        assertEquals("EPS123", result.getNit());
        verify(epsRepository).save(eps);
    }

    @Test
    void registerEPS_DuplicateNIT_Throws() {
        when(epsRepository.existsById("EPS123")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.registerEPS(eps));
        assertTrue(e.getMessage().contains("Ya existe una EPS"));
    }

    @Test
    void registerEPS_NullNIT_Throws() {
        eps.setNit(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.registerEPS(eps));
        assertTrue(e.getMessage().contains("NIT de la EPS es obligatorio"));
    }

    @Test
    void registerEPS_ShortName_Throws() {
        eps.setName("ab");
        when(epsRepository.existsById("EPS123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.registerEPS(eps));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void registerEPS_NullName_Throws() {
        eps.setName(null);
        when(epsRepository.existsById("EPS123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.registerEPS(eps));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // ---------- UPDATE ----------
    @Test
    void updateEPS_Valid() {
        when(epsRepository.existsById("EPS123")).thenReturn(true);
        when(epsRepository.save(eps)).thenReturn(eps);
        EPS updated = epsService.UpdateEPS(eps);
        assertEquals("Salud Total", updated.getName());
    }

    @Test
    void updateEPS_NotFound_Throws() {
        when(epsRepository.existsById("EPS123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.UpdateEPS(eps));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateEPS_InvalidName_Throws() {
        eps.setName("a");
        when(epsRepository.existsById("EPS123")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.UpdateEPS(eps));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // ---------- GET ----------
    @Test
    void getAllEPS_ReturnsList() {
        when(epsRepository.findAll()).thenReturn(allEPS);
        List<EPS> result = epsService.getAllEPS();

        assertEquals(5, result.size());
        assertEquals("EPS123", result.get(0).getNit());
        assertEquals("Nueva EPS", result.get(1).getName());
        assertEquals("EPS Compensar", result.get(2).getName());
        assertEquals("Sura", result.get(3).getName());
        assertEquals("Sanitas", result.get(4).getName());
    }

    @Test
    void getEPSById_Found() {
        when(epsRepository.findById("EPS123")).thenReturn(Optional.of(eps));
        Optional<EPS> result = epsService.getEPSById("EPS123");
        assertTrue(result.isPresent());
        assertEquals("EPS123", result.get().getNit());
    }

    @Test
    void getEPSById_NotFound() {
        when(epsRepository.findById("X")).thenReturn(Optional.empty());
        Optional<EPS> result = epsService.getEPSById("X");
        assertTrue(result.isEmpty());
    }

    // ---------- DELETE ----------
    @Test
    void deleteEPS_Valid() {
        when(epsRepository.existsById("EPS123")).thenReturn(true);
        assertDoesNotThrow(() -> epsService.removeEPSById("EPS123"));
        verify(epsRepository).deleteById("EPS123");
    }

    @Test
    void deleteEPS_NotFound_Throws() {
        when(epsRepository.existsById("X")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> epsService.removeEPSById("X"));
        assertEquals("No se puede eliminar una EPS que no existe.", e.getMessage());
    }
}
