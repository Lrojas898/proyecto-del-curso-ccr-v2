package com.example.ccrHospitalManagement.mapper;

import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDTO toDto(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        dto.setSex(user.getSex());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setSpeciality(user.getSpeciality());
        dto.setEpsName(user.getEps() != null ? user.getEps().getName() : null);
        dto.setPrepaidMedicineName(user.getPrepaidMedicine() != null ? user.getPrepaidMedicine().getName() : null);

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        dto.setRoles(roleNames);

        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setSex(dto.getSex());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setSpeciality(dto.getSpeciality());

        
        Set<Role> roles = dto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        return user;
    }
}
