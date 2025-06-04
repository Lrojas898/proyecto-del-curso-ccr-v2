package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.dto.AssistanceActDTO;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.model.AssistanceAct;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttentionEpisodeMapper {

    private final UserMapper userMapper;
    private final AssistanceActMapper assistanceActMapper;
    private final UserRepository userRepository; // usado en toEntity()

    public AttentionEpisodeDTO toDto(AttentionEpisode episode) {
        if (episode == null) return null;

        AttentionEpisodeDTO dto = new AttentionEpisodeDTO();
        dto.setId(episode.getId());
        dto.setCreationDate(episode.getCreationDate());
        dto.setDiagnosis(episode.getDiagnosis());
        dto.setDescription(episode.getDescription());

        dto.setClinicalHistoryId(
                episode.getClinicalHistory() != null ? episode.getClinicalHistory().getId() : null
        );

        dto.setAppointmentId(
                episode.getAppointment() != null ? episode.getAppointment().getId() : null
        );

        dto.setProtocolId(
                episode.getMedicalProtocol() != null ? episode.getMedicalProtocol().getId() : null
        );
        dto.setProtocolName(
                episode.getMedicalProtocol() != null ? episode.getMedicalProtocol().getName() : null
        );


        dto.setDoctor(userMapper.toDto(episode.getDoctor()));

        List<AssistanceActDTO> acts = episode.getAssistanceActs() != null
                ? episode.getAssistanceActs().stream()
                .map(assistanceActMapper::toDto)
                .collect(Collectors.toList())
                : null;

        dto.setAssistanceActs(acts);

        return dto;
    }

    public AttentionEpisode toEntity(AttentionEpisodeDTO dto) {
        if (dto == null) return null;

        AttentionEpisode entity = new AttentionEpisode();
        entity.setId(dto.getId());
        entity.setCreationDate(dto.getCreationDate());
        entity.setDiagnosis(dto.getDiagnosis());
        entity.setDescription(dto.getDescription());

        // doctor se setea en el servicio, no aquí
        // clinicalHistory y appointment también

        return entity;
    }
}
