package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicalHistoryMapper {
    ClinicalHistoryDTO toDto(ClinicalHistory history);
    ClinicalHistory toEntity(ClinicalHistoryDTO dto);

    AttentionEpisodeDTO toDto(AttentionEpisode episode);
    AttentionEpisode toEntity(AttentionEpisodeDTO dto);

    List<AttentionEpisodeDTO> toDtoList(List<AttentionEpisode> episodes);
    List<AttentionEpisode> toEntityList(List<AttentionEpisodeDTO> dtos);
}
