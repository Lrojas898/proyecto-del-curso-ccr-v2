package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.AppointmentDTO;
import com.example.ccrHospitalManagement.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDTO toDto(Appointment appointment);
    Appointment toEntity(AppointmentDTO dto);
}