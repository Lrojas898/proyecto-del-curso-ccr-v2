package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamType;
import com.example.ccrHospitalManagement.repository.ExamTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamTypeServiceImpl implements ExamTypeService {

    private final ExamTypeRepository examTypeRepository;

    @Override
    public ExamType createExamType(ExamType type) {
        if (examTypeRepository.existsById(type.getId())) {
            throw new IllegalArgumentException("Ya existe un tipo de examen con ese ID.");
        }
        validateExamType(type, true);
        return examTypeRepository.save(type);
    }

    @Override
    public ExamType updateExamType(ExamType type) {
        if (!examTypeRepository.existsById(type.getId())) {
            throw new IllegalArgumentException("El tipo de examen no existe.");
        }
        validateExamType(type, false);
        return examTypeRepository.save(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamType> getAllExamTypes() {
        return examTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamType> getExamTypeById(String id) {
        return examTypeRepository.findById(id);
    }

    @Override
    public void removeExamTypeById(String id) {
        if (!examTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un tipo de examen que no existe.");
        }
        examTypeRepository.deleteById(id);
    }

    private void validateExamType(ExamType type, boolean isCreate) {
        if (isCreate && (type.getId() == null || type.getId().isBlank())) {
            throw new IllegalArgumentException("El ID del tipo de examen es obligatorio.");
        }

        if (type.getName() == null || type.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre del tipo de examen debe tener al menos 3 caracteres.");
        }
    }
}
