package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserRegistrationDto dto);


}
