package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Location;
import com.example.ccrHospitalManagement.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location registerLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> getLocationById(String id) {
        return locationRepository.findById(id);
    }

    @Override
    public Location UpdateLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public void removeLocationById(String id) {
        locationRepository.deleteById(id);
    }
}
