package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.EPS;

import com.example.ccrHospitalManagement.repository.EPSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EpsServiceImpl {
    private final EPSRepository epsRepository;



    public List<EPS> getAllEps() {
        return epsRepository.findAll();
    }
}
