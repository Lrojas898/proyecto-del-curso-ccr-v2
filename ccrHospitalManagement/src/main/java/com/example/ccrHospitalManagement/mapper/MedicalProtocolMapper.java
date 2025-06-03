package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.MedicalProtocolDTO;
import com.example.ccrHospitalManagement.dto.ProtocolTaskDTO;
import com.example.ccrHospitalManagement.model.MedicalProtocol;
import com.example.ccrHospitalManagement.model.ProtocolTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalProtocolMapper {

    public MedicalProtocolDTO toDTO(MedicalProtocol entity) {
        if (entity == null) {
            return null;
        }

        MedicalProtocolDTO dto = new MedicalProtocolDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        if (entity.getTasks() != null) {
            List<ProtocolTaskDTO> taskDTOs = entity.getTasks().stream()
                    .map(this::toTaskDTO)
                    .collect(Collectors.toList());
            dto.setTasks(taskDTOs);
        }

        return dto;
    }

    public MedicalProtocol toEntity(MedicalProtocolDTO dto) {
        if (dto == null) {
            return null;
        }

        MedicalProtocol entity = new MedicalProtocol();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        // Tasks are handled separately to avoid circular references

        return entity;
    }

    public ProtocolTaskDTO toTaskDTO(ProtocolTask entity) {
        if (entity == null) {
            return null;
        }

        ProtocolTaskDTO dto = new ProtocolTaskDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setRequired(entity.isRequired());
        dto.setOrderIndex(entity.getOrderIndex());

        return dto;
    }

    public ProtocolTask toTaskEntity(ProtocolTaskDTO dto, MedicalProtocol protocol) {
        if (dto == null) {
            return null;
        }

        ProtocolTask entity = new ProtocolTask();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setRequired(dto.isRequired());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setProtocol(protocol);

        return entity;
    }

    public List<MedicalProtocolDTO> toDTOList(List<MedicalProtocol> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
