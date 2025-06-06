package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.AssistanceAct;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.repository.AssistanceActRepository;
import com.example.ccrHospitalManagement.repository.AttentionEpisodeRepository;
import com.example.ccrHospitalManagement.repository.ClinicalHistoryRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final UserRepository userRepository;
    private final AttentionEpisodeRepository attentionEpisodeRepository;
    private final AssistanceActRepository assistanceActRepository;

    @Override
    public ClinicalHistory createClinicalHistory(ClinicalHistory history) {
        validateClinicalHistory(history, true);
        return clinicalHistoryRepository.save(history);
    }

    @Override
    public ClinicalHistory UpdateClinicalHistory(ClinicalHistory history) {
        if (!clinicalHistoryRepository.existsById(history.getId())) {
            throw new IllegalArgumentException("La historia clínica no existe.");
        }
        validateClinicalHistory(history, false);
        return clinicalHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClinicalHistory> getAllClinicalHistories() {
        return clinicalHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClinicalHistory> getClinicalHistoryById(Long id) {
        return clinicalHistoryRepository.findById(id);
    }

    @Override
    public void removeClinicalHistoryById(Long id) {
        if (!clinicalHistoryRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una historia clínica que no existe.");
        }
        clinicalHistoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ClinicalHistory> getClinicalHistoryByUsername(String username) {
        return clinicalHistoryRepository.findByUser_Username(username);
    }

    private void validateClinicalHistory(ClinicalHistory history, boolean isCreate) {
        if (history.getDate() == null || history.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha debe ser válida y no puede estar en el futuro.");
        }

        if (history.getHour() == null) {
            throw new IllegalArgumentException("Debe especificarse la hora de registro.");
        }

        if (history.getGeneralObservations() == null || history.getGeneralObservations().trim().length() < 10) {
            throw new IllegalArgumentException("Las observaciones generales deben tener al menos 10 caracteres.");
        }

        if (history.getUser() == null || !userRepository.existsById(history.getUser().getId())) {
            throw new IllegalArgumentException("El usuario asignado no es válido.");
        }

        if (isCreate && clinicalHistoryRepository.existsByUserId(history.getUser().getId())) {
            throw new IllegalArgumentException("El usuario ya tiene una historia clínica registrada.");
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ClinicalHistory> getClinicalHistoryByUserId(String userId) {
        return clinicalHistoryRepository.findByUser_Id(userId);
    }

    @Override
    public AttentionEpisode saveEpisode(AttentionEpisode episode) {
        return attentionEpisodeRepository.save(episode);
    }

    public Optional<ClinicalHistory> getByUsername(String username) {
        return clinicalHistoryRepository.findByUser_Username(username);
    }


    public Optional<ClinicalHistory> getByUserId(String userId) {
        return clinicalHistoryRepository.findByUser_Id(userId);
    }


    @Override
    public AssistanceAct addAssistanceActToEpisode(Long episodeId, AssistanceAct act) {
        AttentionEpisode episode = attentionEpisodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado"));

        act.setAttentionEpisode(episode);
        return assistanceActRepository.save(act);
    }

    @Override
    public Optional<AttentionEpisode> getEpisodeById(Long id) {
        return attentionEpisodeRepository.findById(id);
    }



}
