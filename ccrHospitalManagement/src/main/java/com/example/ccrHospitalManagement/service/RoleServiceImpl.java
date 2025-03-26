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
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Role createRole(Role role, List<String> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        List<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Permiso con ID " + id + " no existe")))
                .collect(Collectors.toList());

        role.setPermissions(new HashSet<>(permissions));
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
    public Role updateRole(Role role, List<String> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        List<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Permiso con ID " + id + " no existe")))
                .collect(Collectors.toList());

        role.setPermissions(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }
}
