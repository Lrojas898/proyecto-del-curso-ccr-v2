package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
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

        System.out.println("Usuario guardado con ID: " + user.getId());


    }

    @Transactional
    public User createUser(User user, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }


    @Transactional
    public User updateUser(User user, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }


    @Transactional
    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id); // Buscar al usuario primero
        if (user.isPresent()) {
            userRepository.deleteById(id); // Si existe, lo eliminamos
        } else {
            // Si no existe, lanzar una excepción o simplemente no hacer nada
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }




}
