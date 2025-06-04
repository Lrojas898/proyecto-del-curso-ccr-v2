package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttentionEpisodeServiceImpl implements AttentionEpisodeService {

    private final AttentionEpisodeRepository episodeRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalProtocolRepository medicalProtocolRepository;

    @Override
    public AttentionEpisode createAttentionEpisode(AttentionEpisode episode) {
        validateEpisode(episode, true);
        return episodeRepository.save(episode);
    }

    @Override
    public AttentionEpisode updateAttentionEpisode(AttentionEpisode episode) {
        if (!episodeRepository.existsById(episode.getId())) {
            throw new IllegalArgumentException("El episodio de atención no existe.");
        }
        validateEpisode(episode, false);
        return episodeRepository.save(episode);
    }

    @Override
    public void removeAttentionEpisodeById(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un episodio que no existe.");
        }
        episodeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttentionEpisode> getAllAttentionEpisodes() {
        return episodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttentionEpisode> getAttentionEpisodeById(Long id) {
        return episodeRepository.findById(id);
    }

    // Nuevos métodos para lógica extendida con roles y validaciones

    @Override
    @Transactional(readOnly = true)
    public List<AttentionEpisode> getAllForCurrentUser() {
        User currentUser = getCurrentUser();
        if (hasRole(currentUser, "ADMIN") || hasRole(currentUser, "DOCTOR")) {
            return episodeRepository.findAll();
        } else if (hasRole(currentUser, "PACIENTE")) {
            return episodeRepository.findByClinicalHistory_User_Id(currentUser.getId());
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttentionEpisode> getByIdIfAuthorized(Long id) {
        Optional<AttentionEpisode> opt = episodeRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        AttentionEpisode ep = opt.get();
        User currentUser = getCurrentUser();
        if (hasRole(currentUser, "ADMIN") || hasRole(currentUser, "DOCTOR")) {
            return Optional.of(ep);
        }

        if (hasRole(currentUser, "PACIENTE")
                && ep.getClinicalHistory().getUser().getId().equals(currentUser.getId())) {
            return Optional.of(ep);
        }

        return Optional.empty();
    }

    @Override
    public AttentionEpisode createAttentionEpisodeWithAssociations(AttentionEpisode episode, AttentionEpisodeDTO dto) {
        validateAndLinkAssociations(episode, dto);
        return createAttentionEpisode(episode);
    }

    @Override
    public AttentionEpisode updateAttentionEpisodeWithAssociations(AttentionEpisode episode, AttentionEpisodeDTO dto) {
        validateAndLinkAssociations(episode, dto);
        return updateAttentionEpisode(episode);
    }

    private void validateAndLinkAssociations(AttentionEpisode episode, AttentionEpisodeDTO dto) {

        // Validar y asignar historia clínica
        if (dto.getClinicalHistoryId() == null) {
            throw new IllegalArgumentException("El ID de la historia clínica no puede ser null.");
        }
        ClinicalHistory history = clinicalHistoryRepository.findById(dto.getClinicalHistoryId())
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));
        episode.setClinicalHistory(history);

        // Validar que el doctor ya esté asignado
        if (episode.getDoctor() == null) {
            throw new IllegalArgumentException("Debe asignarse un médico responsable.");
        }

        // Validar y asignar cita (si existe)
        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
            episode.setAppointment(appointment);
        }

        // Validar y asignar protocolo médico (si existe)
        if (dto.getProtocolId() != null) {
            MedicalProtocol protocol = medicalProtocolRepository.findById(dto.getProtocolId())
                    .orElseThrow(() -> new IllegalArgumentException("Protocolo médico no encontrado"));
            episode.setMedicalProtocol(protocol);
        }
    }



    private void validateEpisode(AttentionEpisode episode, boolean isCreate) {
        if (episode.getCreationDate() == null) {
            throw new IllegalArgumentException("Debe establecerse la fecha de creación.");
        }

        if (episode.getCreationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede ser futura.");
        }

        if (episode.getDiagnosis() == null || episode.getDiagnosis().trim().length() < 10) {
            throw new IllegalArgumentException("El diagnóstico debe tener al menos 10 caracteres.");
        }

        if (episode.getDescription() == null || episode.getDescription().trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }

        if (episode.getClinicalHistory() == null) {
            throw new IllegalArgumentException("Debe asociarse a una historia clínica.");
        }

        if (episode.getDoctor() == null) {
            throw new IllegalArgumentException("Debe asignarse un médico responsable.");
        }
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuario autenticado no encontrado");
        }
        return user;
    }


    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_" + roleName));
    }
}
