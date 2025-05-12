package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import com.example.ccrHospitalManagement.repository.AssistanceActRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssistanceActServiceImpl implements AssistanceActService {

    private final AssistanceActRepository assistanceActRepository;

    @Override
    public AssistanceAct createAssistanceAct(AssistanceAct act) {
        validateAssistanceAct(act, true);
        return assistanceActRepository.save(act);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssistanceAct> getAllAssistanceActs() {
        return assistanceActRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssistanceAct> getAssistanceActById(Long id) {
        return assistanceActRepository.findById(id);
    }

    @Override
    public AssistanceAct UpdateAssistanceAct(AssistanceAct act) {
        if (!assistanceActRepository.existsById(act.getId())) {
            throw new IllegalArgumentException("No se puede actualizar un acto asistencial que no existe.");
        }
        validateAssistanceAct(act, false);
        return assistanceActRepository.save(act);
    }

    @Override
    public void removeAssistanceActById(Long id) {
        if (!assistanceActRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un acto asistencial que no existe.");
        }
        assistanceActRepository.deleteById(id);
    }

    private void validateAssistanceAct(AssistanceAct act, boolean isCreate) {

        if (act.getIssueDate() == null) {
            throw new IllegalArgumentException("Debe proporcionarse la fecha de emisión del acto.");
        }

        LocalDate issueDate = act.getIssueDate().toLocalDate();
        if (issueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del acto no puede estar en el futuro.");
        }

        if (act.getDescription() == null || act.getDescription().trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }

        if (act.getType() == null) {
            throw new IllegalArgumentException("Debe especificarse el tipo de acto asistencial.");
        }

        if (act.getAttentionEpisode() == null) {
            throw new IllegalArgumentException("Debe estar vinculado a un episodio de atención.");
        }
    }
}
