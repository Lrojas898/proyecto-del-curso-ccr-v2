package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceActType;
import com.example.ccrHospitalManagement.repository.AssistanceActTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssistanceActTypeServiceImpl implements AssistanceActTypeService {

    private final AssistanceActTypeRepository assistanceActTypeRepository;

    @Override
    public AssistanceActType createAssistanceActType(AssistanceActType type) {
        validateType(type, true);
        return assistanceActTypeRepository.save(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssistanceActType> getAllAssistanceActTypes() {
        return assistanceActTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssistanceActType> getAssistanceActTypeById(Long id) {
        return assistanceActTypeRepository.findById(id);
    }

    @Override
    public AssistanceActType UpdateAssistanceActType(AssistanceActType type) {
        if (!assistanceActTypeRepository.existsById(type.getId())) {
            throw new IllegalArgumentException("El tipo de acto asistencial no existe.");
        }
        validateType(type, false);
        return assistanceActTypeRepository.save(type);
    }

    @Override
    public void removeAssistanceActTypeById(Long id) {
        if (!assistanceActTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un tipo que no existe.");
        }
        assistanceActTypeRepository.deleteById(id);
    }

    private void validateType(AssistanceActType type, boolean isCreate) {

        if (type.getName() == null || type.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre del tipo de acto debe tener al menos 3 caracteres.");
        }
    }
}
