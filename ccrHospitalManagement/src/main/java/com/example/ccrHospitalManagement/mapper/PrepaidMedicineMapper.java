package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.PrepaidMedicineDTO;
import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrepaidMedicineMapper {
    PrepaidMedicineDTO toDto(PrepaidMedicine medicine);
    PrepaidMedicine toEntity(PrepaidMedicineDTO dto);
}
