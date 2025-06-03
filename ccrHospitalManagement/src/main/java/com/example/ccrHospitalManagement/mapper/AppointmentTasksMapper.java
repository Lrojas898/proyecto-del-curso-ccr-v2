package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AppointmentTaskDTO;
import com.example.ccrHospitalManagement.dto.AppointmentTasksDTO;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.AppointmentTask;
import com.example.ccrHospitalManagement.model.AppointmentTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentTasksMapper {

    private final MedicalProtocolMapper medicalProtocolMapper;

    @Autowired
    public AppointmentTasksMapper(MedicalProtocolMapper medicalProtocolMapper) {
        this.medicalProtocolMapper = medicalProtocolMapper;
    }

    public AppointmentTasksDTO toDTO(AppointmentTasks entity) {
        if (entity == null) {
            return null;
        }

        AppointmentTasksDTO dto = new AppointmentTasksDTO();
        dto.setId(entity.getId());
        dto.setAppointmentId(entity.getAppointment().getId());
        dto.setProtocol(medicalProtocolMapper.toDTO(entity.getProtocol()));

        if (entity.getTasks() != null) {
            List<AppointmentTaskDTO> taskDTOs = entity.getTasks().stream()
                    .map(this::toTaskDTO)
                    .collect(Collectors.toList());
            dto.setTasks(taskDTOs);
        }

        return dto;
    }

    public AppointmentTasks toEntity(AppointmentTasksDTO dto, Appointment appointment) {
        if (dto == null) {
            return null;
        }

        AppointmentTasks entity = new AppointmentTasks();
        entity.setId(dto.getId());
        entity.setAppointment(appointment);
        entity.setProtocol(medicalProtocolMapper.toEntity(dto.getProtocol()));

        // Tasks are handled separately to avoid circular references

        return entity;
    }

    public AppointmentTaskDTO toTaskDTO(AppointmentTask entity) {
        if (entity == null) {
            return null;
        }

        AppointmentTaskDTO dto = new AppointmentTaskDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setRequired(entity.isRequired());
        dto.setCompleted(entity.isCompleted());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setCompletedBy(entity.getCompletedBy());
        dto.setNotes(entity.getNotes());

        return dto;
    }

    public AppointmentTask toTaskEntity(AppointmentTaskDTO dto, AppointmentTasks appointmentTasks) {
        if (dto == null) {
            return null;
        }

        AppointmentTask entity = new AppointmentTask();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setRequired(dto.isRequired());
        entity.setCompleted(dto.isCompleted());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setCompletedBy(dto.getCompletedBy());
        entity.setNotes(dto.getNotes());
        entity.setAppointmentTasks(appointmentTasks);

        return entity;
    }

    public List<AppointmentTasksDTO> toDTOList(List<AppointmentTasks> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
