package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.repository.LocationRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public AppointmentDTO toDto(Appointment entity) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setStartTime(entity.getStartTime());
        dto.setDescription(entity.getDescription());
        dto.setLocationId(entity.getLocation().getId());
        dto.setPatientId(entity.getPatient().getId());
        dto.setDoctorId(entity.getDoctor().getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Appointment toEntity(AppointmentDTO dto) {
        Appointment entity = new Appointment();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setStartTime(dto.getStartTime());
        entity.setDescription(dto.getDescription());
        entity.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada")));
        entity.setPatient(userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado")));
        entity.setDoctor(userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado")));
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
