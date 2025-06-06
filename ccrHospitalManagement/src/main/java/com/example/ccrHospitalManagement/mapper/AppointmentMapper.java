package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.dto.LocationDTO;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserMapper userMapper;

    public AppointmentDTO toDto(Appointment entity) {
        AppointmentDTO dto = new AppointmentDTO();

        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setStartTime(entity.getStartTime());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());

        // Doctor
        dto.setDoctorId(entity.getDoctor().getId());
        dto.setDoctorFirstName(entity.getDoctor().getFirstName());
        dto.setDoctorLastName(entity.getDoctor().getLastName());
        dto.setDoctorSpeciality(entity.getDoctor().getSpeciality());
        dto.setDoctor(userMapper.toDto(entity.getDoctor()));

        // Patient
        dto.setPatientId(entity.getPatient().getId());
        dto.setPatientFirstName(entity.getPatient().getFirstName());
        dto.setPatientLastName(entity.getPatient().getLastName());
        dto.setPatient(userMapper.toDto(entity.getPatient()));

        // Location
        dto.setLocationId(entity.getLocation().getId());
        dto.setLocationName(entity.getLocation().getName());

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(entity.getLocation().getId());
        locationDTO.setName(entity.getLocation().getName());
        locationDTO.setAddress(entity.getLocation().getAddress());
        locationDTO.setDescription(entity.getLocation().getDescription());
        dto.setLocation(locationDTO);

        return dto;
    }

    public Appointment toEntity(AppointmentDTO dto) {
        Appointment entity = new Appointment();

        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setStartTime(dto.getStartTime());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());

        entity.setDoctor(userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado")));

        entity.setPatient(userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado")));

        entity.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada")));

        return entity;
    }
}
