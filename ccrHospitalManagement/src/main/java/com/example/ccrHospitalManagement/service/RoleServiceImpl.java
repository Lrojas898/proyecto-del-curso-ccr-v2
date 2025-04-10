package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Permission;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.repository.PermissionRepository;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createRole(Role role) {
        // Simplemente guardar el rol sin permisos
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(String id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public Role updateRole(Role role) {
        // Verificar que el rol existe antes de actualizar
        if (!roleRepository.existsById(role.getId())) {
            throw new IllegalArgumentException("El rol con ID " + role.getId() + " no existe.");
        }
        // Actualizar el rol sin permisos
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("El rol con ID " + id + " no existe.");
        }
        roleRepository.deleteById(id);
    }
}
