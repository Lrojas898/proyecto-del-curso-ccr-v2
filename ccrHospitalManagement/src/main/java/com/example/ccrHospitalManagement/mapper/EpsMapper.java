package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.EPSDTO;
import com.example.ccrHospitalManagement.model.EPS;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EpsMapper {

    EPSDTO toDto(EPS eps);

    EPS toEntity(EPSDTO dto);
}
