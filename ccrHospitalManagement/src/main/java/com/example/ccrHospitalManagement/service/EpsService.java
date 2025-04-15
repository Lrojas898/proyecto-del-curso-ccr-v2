package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.EPS;
import java.util.List;
import java.util.Optional;

public interface EpsService {

    EPS registerEPS(EPS eps);
    List<EPS> getAllEPS();
    Optional<EPS> getEPSById(String id);
    EPS UpdateEPS(EPS eps);
    void removeEPSById(String id);
}
