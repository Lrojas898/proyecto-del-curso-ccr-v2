package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamResult;
import com.example.ccrHospitalManagement.repository.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamResultRepository examResultRepository;

    @Override
    public ExamResult createExamResult(ExamResult result) {
        validateExamResult(result, true);
        return examResultRepository.save(result);
    }

    @Override
    public ExamResult updateExamResult(ExamResult result) {
        if (!examResultRepository.existsById(result.getId())) {
            throw new IllegalArgumentException("El resultado de examen no existe.");
        }
        validateExamResult(result, false);
        return examResultRepository.save(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResult> getAllExamResults() {
        return examResultRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamResult> getExamResultById(Long id) {
        return examResultRepository.findById(id);
    }

    @Override
    public void removeExamResultById(Long id) {
        if (!examResultRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un resultado que no existe.");
        }
        examResultRepository.deleteById(id);
    }

    private void validateExamResult(ExamResult result, boolean isCreate) {

        if (result.getResultDate() == null || result.getResultDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del resultado es inválida o futura.");
        }

        if (result.getDescription() == null || result.getDescription().trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }

        if (result.getExamType() == null) {
            throw new IllegalArgumentException("Debe especificarse el tipo de examen.");
        }

        if (result.getPatient() == null || result.getTechnician() == null) {
            throw new IllegalArgumentException("Debe asignarse tanto un paciente como un técnico.");
        }

        if (result.getPatient().getId().equals(result.getTechnician().getId())) {
            throw new IllegalArgumentException("El paciente y el técnico no pueden ser la misma persona.");
        }
    }
}
