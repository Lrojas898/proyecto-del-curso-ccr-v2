package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionService {
    Permission createPermission(Permission permission);
    List<Permission> getAllPermissions();
    Optional<Permission> getPermissionById(String id);
    Permission updatePermission(Permission permission);
    void deletePermission(String id);
}
