package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role updateRole(Role role);
    void deleteRole(Long id);
}
