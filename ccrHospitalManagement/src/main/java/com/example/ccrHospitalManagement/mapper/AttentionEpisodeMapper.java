package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AssistanceActMapper.class })
public interface AttentionEpisodeMapper {
    AttentionEpisodeDTO toDto(AttentionEpisode episode);
    AttentionEpisode toEntity(AttentionEpisodeDTO dto);
}