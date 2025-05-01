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
        location.setId(1L);
        location.setName("Consultorio Norte");
        location.setAddress("Av. Siempre Viva 742");
        location.setDescription("Ubicación para consulta externa");
    }

    // Create

    @Test
    void registerLocation_Valid() {
        when(locationRepository.existsById(1L)).thenReturn(false);
        when(locationRepository.save(location)).thenReturn(location);
        Location result = locationService.registerLocation(location);
        assertNotNull(result);
        verify(locationRepository).save(location);
    }

    @Test
    void registerLocation_ShortName_Throws() {
        location.setName("AB");
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("al menos 3 caracteres"));
    }

    @Test
    void registerLocation_ShortAddress_Throws() {
        location.setAddress("123");
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("al menos 5 caracteres"));
    }

    @Test
    void registerLocation_EmptyDescription_Throws() {
        location.setDescription("  ");
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("descripción, no puede estar vacía"));
    }

    @Test
    void registerLocation_NullName_Throws() {
        location.setName(null);
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("nombre"));
    }

    @Test
    void registerLocation_NullAddress_Throws() {
        location.setAddress(null);
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> locationService.registerLocation(location));
        assertTrue(e.getMessage().contains("dirección"));
    }

    @Test
    void registerLocation_DescriptionIsNull_DoesNotThrow() {
        location.setDescription(null);  
        when(locationRepository.existsById(1L)).thenReturn(false);
        when(locationRepository.save(location)).thenReturn(location);
        Location result = locationService.registerLocation(location);
        assertNotNull(result);
    }


    // Update

    @Test
    void updateLocation_Valid() {
        when(locationRepository.existsById(1L)).thenReturn(true);
        when(locationRepository.save(location)).thenReturn(location);
        Location result = locationService.updateLocation(location);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateLocation_NotFound_Throws() {
        when(locationRepository.existsById(1L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(location));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void updateLocation_InvalidAddress_Throws() {
        location.setAddress("xyz");
        when(locationRepository.existsById(1L)).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(location));
        assertTrue(e.getMessage().contains("al menos 5 caracteres"));
    }

    // Get

    @Test
    void getAllLocations_ReturnsList() {
        when(locationRepository.findAll()).thenReturn(List.of(location));
        List<Location> result = locationService.getAllLocations();
        assertEquals(1, result.size());
    }

    @Test
    void getLocationById_Found() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Optional<Location> result = locationService.getLocationById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getLocationById_NotFound() {
        when(locationRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Location> result = locationService.getLocationById(99L);
        assertTrue(result.isEmpty());
    }

    // Delete

    @Test
    void deleteLocation_Valid() {
        when(locationRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> locationService.removeLocationById(1L));
        verify(locationRepository).deleteById(1L);
    }

    @Test
    void deleteLocation_NotFound_Throws() {
        when(locationRepository.existsById(45L)).thenReturn(false);
        Exception e = assertThrows(IllegalArgumentException.class, () -> locationService.removeLocationById(45L));
        assertTrue(e.getMessage().toLowerCase().contains("no se puede eliminar"));
    }
}
