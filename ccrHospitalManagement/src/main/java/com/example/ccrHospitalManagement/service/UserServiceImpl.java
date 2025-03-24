package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.model.UserRole;
import com.example.ccrHospitalManagement.model.UserRoleId;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public User createUser(User user, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new java.util.HashSet<>(roles));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateUser(User user, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new java.util.HashSet<>(roles));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {

        userRepository.deleteById(id);
    }
}
