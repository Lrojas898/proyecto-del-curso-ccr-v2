package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.ExamResult;
import com.example.ccrHospitalManagement.repository.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public ExamResult createExamResult(ExamResult result, Authentication auth) {
        // Validación: solo técnicos pueden crear
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LABTECH"))) {
            throw new IllegalArgumentException("Solo un técnico de laboratorio puede crear un resultado de examen.");
        }

        validateExamResult(result, true);

        // Asegurar relación bidireccional entre resultado y sus detalles
        if (result.getResults() != null) {
            for (var detail : result.getResults()) {
                detail.setExamResult(result);
            }
        }

        return examResultRepository.save(result);
    }


    @Override
    public ExamResult updateExamResult(ExamResult result, Authentication auth) {
        // Verifica que el examen exista
        ExamResult existing = examResultRepository.findById(result.getId())
                .orElseThrow(() -> new IllegalArgumentException("El resultado de examen no existe."));

        // Roles del usuario autenticado
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isLabtech = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LABTECH"));

        // Verifica permisos
        if (!isAdmin && !isLabtech) {
            throw new IllegalArgumentException("No autorizado para modificar este examen.");
        }

        // Si es técnico, solo puede editar sus propios exámenes
        if (isLabtech) {
            String username = auth.getName(); // usuario logueado
            if (!existing.getTechnician().getUsername().equals(username)) {
                throw new IllegalArgumentException("Solo puedes modificar exámenes que tú creaste.");
            }
        }

        // Validación de campos
        validateExamResult(result, false);

        // Relaciona detalles con el examen
        if (result.getResults() != null) {
            for (var detail : result.getResults()) {
                detail.setExamResult(result);
            }
        }

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
    public long countAllExamResults() {
        return examResultRepository.count();
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

    public List<ExamResult> getExamResultsByUsername(String username) {
    return examResultRepository.findByPatient_Username(username);
}

}
