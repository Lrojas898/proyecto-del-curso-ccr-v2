package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.MedicalProtocol;
import com.example.ccrHospitalManagement.model.ProtocolTask;
import com.example.ccrHospitalManagement.repository.MedicalProtocolRepository;
import com.example.ccrHospitalManagement.repository.ProtocolTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalProtocolServiceImpl implements MedicalProtocolService {

    private final MedicalProtocolRepository medicalProtocolRepository;
    private final ProtocolTaskRepository protocolTaskRepository;

    @Autowired
    public MedicalProtocolServiceImpl(
            MedicalProtocolRepository medicalProtocolRepository,
            ProtocolTaskRepository protocolTaskRepository) {
        this.medicalProtocolRepository = medicalProtocolRepository;
        this.protocolTaskRepository = protocolTaskRepository;
    }

    @Override
    public List<MedicalProtocol> getAllProtocols() {
        return medicalProtocolRepository.findAll();
    }

    @Override
    public Optional<MedicalProtocol> getProtocolById(Long id) {
        return medicalProtocolRepository.findById(id);
    }

    @Override
    public List<MedicalProtocol> searchProtocolsByName(String name) {
        return medicalProtocolRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public MedicalProtocol saveProtocol(MedicalProtocol protocol) {
        return medicalProtocolRepository.save(protocol);
    }

    @Override
    @Transactional
    public void deleteProtocol(Long id) {
        medicalProtocolRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProtocolTask addTaskToProtocol(Long protocolId, ProtocolTask task) {
        Optional<MedicalProtocol> protocolOpt = medicalProtocolRepository.findById(protocolId);
        if (!protocolOpt.isPresent()) {
            throw new RuntimeException("Protocolo no encontrado con ID: " + protocolId);
        }

        MedicalProtocol protocol = protocolOpt.get();
        task.setProtocol(protocol);
        return protocolTaskRepository.save(task);
    }

    @Override
    @Transactional
    public boolean deleteTaskFromProtocol(Long protocolId, Long taskId) {
        Optional<ProtocolTask> taskOpt = protocolTaskRepository.findById(taskId);
        if (!taskOpt.isPresent() || !taskOpt.get().getProtocol().getId().equals(protocolId)) {
            return false;
        }

        protocolTaskRepository.deleteById(taskId);
        return true;
    }
}
