package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import java.util.List;
import java.util.Optional;

public interface AssistanceActService {

    AssistanceAct createAssistanceAct(AssistanceAct assistanceAct);
    List<AssistanceAct> getAllAssistanceActs();
    Optional<AssistanceAct> getAssistanceActById(Long id);
    AssistanceAct UpdateAssistanceAct(AssistanceAct assistanceAct);
    void removeAssistanceActById(Long id);
}
