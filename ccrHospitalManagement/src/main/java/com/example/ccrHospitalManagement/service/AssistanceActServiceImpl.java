
package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import com.example.ccrHospitalManagement.repository.AssistanceActRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssistanceActServiceImpl implements AssistanceActService {

    @Autowired
    private AssistanceActRepository assistanceActRepository;

    @Override
    public AssistanceAct createAssistanceAct(AssistanceAct assistanceAct) {
        return assistanceActRepository.save(assistanceAct);
    }

    @Override
    public List<AssistanceAct> getAllAssistanceActs() {
        return assistanceActRepository.findAll();
    }

    @Override
    public Optional<AssistanceAct> getAssistanceActById(String id) {
        return assistanceActRepository.findById(id);
    }

    @Override
    public AssistanceAct UpdateAssistanceAct(AssistanceAct assistanceAct) {
        return assistanceActRepository.save(assistanceAct);
    }

    @Override
    public void removeAssistanceActById(String id) {
        assistanceActRepository.deleteById(id);
    }
}
