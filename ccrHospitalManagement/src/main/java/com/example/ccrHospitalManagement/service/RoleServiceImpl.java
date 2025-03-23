package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createRole(Role role, List<String> permissionIds) {

        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

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

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }
}
