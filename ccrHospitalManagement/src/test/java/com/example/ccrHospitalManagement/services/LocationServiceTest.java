package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Location;
import com.example.ccrHospitalManagement.repository.LocationRepository;
import com.example.ccrHospitalManagement.service.LocationServiceImpl;
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
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId("LOC123");
        location.setName("Consultorio Norte");
        location.setAddress("Av. Siempre Viva 742");
        location.setDescription("Ubicación para consulta externa");
    }

    // ----------- CREATE -----------

    @Test
    void registerLocation_Valid() {
        when(locationRepository.existsById("LOC123")).thenReturn(false);
        when(locationRepository.save(location)).thenReturn(location);
        Location result = locationService.registerLocation(location);
        assertNotNull(result);
        verify(locationRepository).save(location);
    }

    @Test
    void registerLocation_DuplicateId_Throws() {
        when(locationRepository.existsById("LOC123")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("Ya existe una ubicación"));
    }

    @Test
    void registerLocation_NullId_Throws() {
        location.setId(null);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("ID de la ubicación"));
    }

    @Test
    void registerLocation_ShortName_Throws() {
        location.setName("AB");
        when(locationRepository.existsById("LOC123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void registerLocation_ShortAddress_Throws() {
        location.setAddress("123");
        when(locationRepository.existsById("LOC123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("al menos 5 caracteres"));
    }

    @Test
    void registerLocation_EmptyDescription_Throws() {
        location.setDescription("  ");
        when(locationRepository.existsById("LOC123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("descripción, no puede estar vacía"));
    }

    // ----------- UPDATE -----------

    @Test
    void updateLocation_Valid() {
        when(locationRepository.existsById("LOC123")).thenReturn(true);
        when(locationRepository.save(location)).thenReturn(location);
        Location result = locationService.updateLocation(location);
        assertEquals("LOC123", result.getId());
    }

    @Test
    void updateLocation_NotFound_Throws() {
        when(locationRepository.existsById("LOC123")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(location));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateLocation_InvalidAddress_Throws() {
        location.setAddress("xyz");
        when(locationRepository.existsById("LOC123")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(location));
        assertTrue(e.getMessage().contains("al menos 5 caracteres"));
    }

    // ----------- GET -----------

    @Test
    void getAllLocations_ReturnsList() {
        when(locationRepository.findAll()).thenReturn(List.of(location));
        List<Location> result = locationService.getAllLocations();
        assertEquals(1, result.size());
    }

    @Test
    void getLocationById_Found() {
        when(locationRepository.findById("LOC123")).thenReturn(Optional.of(location));
        Optional<Location> result = locationService.getLocationById("LOC123");
        assertTrue(result.isPresent());
    }

    @Test
    void getLocationById_NotFound() {
        when(locationRepository.findById("X")).thenReturn(Optional.empty());
        Optional<Location> result = locationService.getLocationById("X");
        assertTrue(result.isEmpty());
    }

    // ----------- DELETE -----------

    @Test
    void deleteLocation_Valid() {
        when(locationRepository.existsById("LOC123")).thenReturn(true);
        assertDoesNotThrow(() -> locationService.removeLocationById("LOC123"));
        verify(locationRepository).deleteById("LOC123");
    }

    @Test
    void deleteLocation_NotFound_Throws() {
        when(locationRepository.existsById("LOC999")).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.removeLocationById("LOC999"));
        assertTrue(e.getMessage().toLowerCase().contains("no se puede eliminar"));
    }
}
