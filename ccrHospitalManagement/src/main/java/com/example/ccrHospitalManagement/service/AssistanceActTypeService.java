package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import java.util.List;
import java.util.Optional;

public interface AssistanceActTypeService {

    AssistanceActType createAssistanceActType(AssistanceActType type);
    List<AssistanceActType> getAllAssistanceActTypes();
    Optional<AssistanceActType> getAssistanceActTypeById(String id);
    AssistanceActType UpdateAssistanceActType(AssistanceActType type);
    void removeAssistanceActTypeById(String id);
}
