package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Location;
import com.example.ccrHospitalManagement.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location registerLocation(Location location) {
        validateLocation(location, true);
        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Location location) {
        if (!locationRepository.existsById(location.getId())) {
            throw new IllegalArgumentException("La ubicación no existe.");
        }
        validateLocation(location, false);
        return locationRepository.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public void removeLocationById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una ubicación que no existe.");
        }
        locationRepository.deleteById(id);
    }

    private void validateLocation(Location location, boolean isCreate) {

        if (location.getName() == null || location.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }

        if (location.getAddress() == null || location.getAddress().trim().length() < 5) {
            throw new IllegalArgumentException("La dirección debe tener al menos 5 caracteres.");
        }

        if (location.getDescription() != null && location.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Si se proporciona una descripción, no puede estar vacía.");
        }
    }
}
