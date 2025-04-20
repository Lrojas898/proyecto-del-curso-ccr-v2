package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Location;
import java.util.List;
import java.util.Optional;

public interface LocationService {

    Location registerLocation(Location location);
    List<Location> getAllLocations();
    Optional<Location> getLocationById(String id);
    Location updateLocation(Location location);
    void removeLocationById(String id);
}
