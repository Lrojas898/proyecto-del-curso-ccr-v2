package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.EPSRepository;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserRegistrationDto dto) {
        if (userRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("El ID ya existe.");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        EPS eps = epsRepository.findById(dto.getEpsNit())
                .orElseThrow(() -> new IllegalArgumentException("EPS no encontrada"));
        PrepaidMedicine prepaid = prepaidRepository.findById(dto.getPrepaidMedicineNit())
                .orElseThrow(() -> new IllegalArgumentException("Medicina prepagada no encontrada"));

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setSex(dto.getSex());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setEps(eps);
        user.setPrepaidMedicine(prepaid);

        userRepository.save(user);
    }
}
