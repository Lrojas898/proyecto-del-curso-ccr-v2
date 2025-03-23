package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(Role role, List<String> permissionIds);
    List<Role> getAllRoles();
    Optional<Role> getRoleById(String id);
    Role updateRole(Role role, List<String> permissionIds);
    void deleteRole(String id);
}
