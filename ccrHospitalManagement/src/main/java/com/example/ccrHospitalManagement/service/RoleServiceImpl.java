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
    public Role createRole(Role role) {
        if (roleRepository.existsById(role.getId())) {
            throw new IllegalArgumentException("Ya existe un rol con ese ID.");
        }
        validateRole(role, true);
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public Role updateRole(Role role) {
        if (!roleRepository.existsById(role.getId())) {
            throw new IllegalArgumentException("El rol con ID " + role.getId() + " no existe.");
        }
        validateRole(role, false);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("El rol con ID " + id + " no existe.");
        }
        roleRepository.deleteById(id);
    }

    private void validateRole(Role role, boolean isCreate) {

        if (role.getName() == null || role.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre del rol debe tener al menos 3 caracteres.");
        }
    }
}
