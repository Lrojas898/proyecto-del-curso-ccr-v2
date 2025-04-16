package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import com.example.ccrHospitalManagement.service.PrepaidMedicineServiceImpl;
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
public class PrepaidMedicineServiceTest {

    @Mock
    private PrepaidMedicineRepository repository;

    @InjectMocks
    private PrepaidMedicineServiceImpl service;

    private PrepaidMedicine medicine;
    private PrepaidMedicine medicine2;
    private PrepaidMedicine medicine3;
    private List<PrepaidMedicine> allMedicines;

    @BeforeEach
    void setUp() {
        medicine = new PrepaidMedicine("PRE123", "Colsanitas");
        medicine2 = new PrepaidMedicine("PRE456", "MedPlus");
        medicine3 = new PrepaidMedicine("PRE789", "NuevaVida");

        allMedicines = List.of(medicine, medicine2, medicine3);
    }

    // Create
    @Test
    void registerPrepaidMedicine_Valid() {
        when(repository.existsById("PRE123")).thenReturn(false);
        when(repository.save(medicine)).thenReturn(medicine);

        PrepaidMedicine result = service.registerPrepaidMedicine(medicine);
        assertNotNull(result);
        assertEquals("PRE123", result.getNit());
        verify(repository).save(medicine);
    }

    @Test
    void registerPrepaidMedicine_DuplicateNIT_Throws() {
        when(repository.existsById("PRE123")).thenReturn(true);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.registerPrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("Ya existe una medicina"));
    }

    @Test
    void registerPrepaidMedicine_NullNIT_Throws() {
        medicine.setNit(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.registerPrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("NIT es obligatorio"));
    }

    @Test
    void registerPrepaidMedicine_ShortName_Throws() {
        medicine.setName("ab");
        when(repository.existsById("PRE123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.registerPrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void registerPrepaidMedicine_NullName_Throws() {
        medicine.setName(null);
        when(repository.existsById("PRE123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.registerPrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void registerPrepaidMedicine_BlankNIT_Throws() {
        medicine.setNit("   "); 
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> service.registerPrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("NIT es obligatorio"));
    }


    // Update
    @Test
    void updatePrepaidMedicine_Valid() {
        when(repository.existsById("PRE123")).thenReturn(true);
        when(repository.save(medicine)).thenReturn(medicine);

        PrepaidMedicine updated = service.updatePrepaidMedicine(medicine);
        assertEquals("Colsanitas", updated.getName());
    }

    @Test
    void updatePrepaidMedicine_NotFound_Throws() {
        when(repository.existsById("PRE123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.updatePrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updatePrepaidMedicine_InvalidName_Throws() {
        medicine.setName("a");
        when(repository.existsById("PRE123")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> service.updatePrepaidMedicine(medicine));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    // Get
    @Test
    void getAllPrepaidMedicines_ReturnsList() {
        when(repository.findAll()).thenReturn(allMedicines);
        List<PrepaidMedicine> result = service.getAllPrepaidMedicines();
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("Colsanitas")));
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("MedPlus")));
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("NuevaVida")));
    }

    @Test
    void getPrepaidMedicineById_Found() {
        when(repository.findById("PRE123")).thenReturn(Optional.of(medicine));
        Optional<PrepaidMedicine> result = service.getPrepaidMedicineById("PRE123");
        assertTrue(result.isPresent());
        assertEquals("Colsanitas", result.get().getName());
    }

    @Test
    void getPrepaidMedicineById_NotFound() {
        when(repository.findById("X")).thenReturn(Optional.empty());
        Optional<PrepaidMedicine> result = service.getPrepaidMedicineById("X");
        assertTrue(result.isEmpty());
    }

    // Delete
    @Test
    void deletePrepaidMedicine_Valid() {
        when(repository.existsById("PRE123")).thenReturn(true);
        assertDoesNotThrow(() -> service.removePrepaidMedicineById("PRE123"));
        verify(repository).deleteById("PRE123");
    }

    @Test
    void deletePrepaidMedicine_NotFound_Throws() {
        when(repository.existsById("X")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.removePrepaidMedicineById("X")
        );

        assertEquals("No se puede eliminar una medicina prepagada que no existe.", exception.getMessage());
    }

}
