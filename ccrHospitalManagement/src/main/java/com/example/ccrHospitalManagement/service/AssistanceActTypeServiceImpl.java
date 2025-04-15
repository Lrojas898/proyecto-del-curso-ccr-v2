package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import com.example.ccrHospitalManagement.repository.AssistanceActTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssistanceActTypeServiceImpl implements AssistanceActTypeService {

    @Autowired
    private AssistanceActTypeRepository assistanceActTypeRepository;

    @Override
    public AssistanceActType createAssistanceActType(AssistanceActType type) {
        return assistanceActTypeRepository.save(type);
    }

    @Override
    public List<AssistanceActType> getAllAssistanceActTypes() {
        return assistanceActTypeRepository.findAll();
    }

    @Override
    public Optional<AssistanceActType> getAssistanceActTypeById(String id) {
        return assistanceActTypeRepository.findById(id);
    }

    @Override
    public AssistanceActType UpdateAssistanceActType(AssistanceActType type) {
        return assistanceActTypeRepository.save(type);
    }

    @Override
    public void removeAssistanceActTypeById(String id) {
        assistanceActTypeRepository.deleteById(id);
    }
}
