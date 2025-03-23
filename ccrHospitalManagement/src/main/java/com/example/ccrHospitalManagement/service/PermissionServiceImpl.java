package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Permission;
import com.example.ccrHospitalManagement.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> getPermissionById(String id) {
        return permissionRepository.findById(id);
    }

    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }
}
