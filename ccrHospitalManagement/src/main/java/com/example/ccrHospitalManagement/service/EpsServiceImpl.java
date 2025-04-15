package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.repository.EPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpsServiceImpl implements EpsService {

    @Autowired
    private EPSRepository epsRepository;

    @Override
    public EPS registerEPS(EPS eps) {
        return epsRepository.save(eps);
    }

    @Override
    public List<EPS> getAllEPS() {
        return epsRepository.findAll();
    }

    @Override
    public Optional<EPS> getEPSById(String id) {
        return epsRepository.findById(id);
    }

    @Override
    public EPS UpdateEPS(EPS eps) {
        return epsRepository.save(eps);
    }

    @Override
    public void removeEPSById(String id) {
        epsRepository.deleteById(id);
    }
}
