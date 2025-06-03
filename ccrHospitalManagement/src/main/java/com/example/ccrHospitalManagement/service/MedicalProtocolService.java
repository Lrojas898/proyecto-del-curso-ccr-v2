package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.MedicalProtocol;
import com.example.ccrHospitalManagement.model.ProtocolTask;

import java.util.List;
import java.util.Optional;

public interface MedicalProtocolService {

    List<MedicalProtocol> getAllProtocols();

    Optional<MedicalProtocol> getProtocolById(Long id);

    List<MedicalProtocol> searchProtocolsByName(String name);

    MedicalProtocol saveProtocol(MedicalProtocol protocol);

    void deleteProtocol(Long id);

    ProtocolTask addTaskToProtocol(Long protocolId, ProtocolTask task);

    boolean deleteTaskFromProtocol(Long protocolId, Long taskId);
}
