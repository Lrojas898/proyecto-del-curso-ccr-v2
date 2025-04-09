package com.example.ccrHospitalManagement.dto;

import java.util.Set;

public class UserRoleDTO {
    private String userId;
    private String username;
    private Set<String> assignedRoleIds; // estos son los roles que el usuario tiene
    private Set<RoleDTO> availableRoles; // roles disponibles

    public UserRoleDTO() {}

    public UserRoleDTO(String userId, String username, Set<String> assignedRoleIds, Set<RoleDTO> availableRoles) {
        this.userId = userId;
        this.username = username;
        this.assignedRoleIds = assignedRoleIds;
        this.availableRoles = availableRoles;
    }

    // Getters y setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Set<String> getAssignedRoleIds() { return assignedRoleIds; }
    public void setAssignedRoleIds(Set<String> assignedRoleIds) { this.assignedRoleIds = assignedRoleIds; }
    public Set<RoleDTO> getAvailableRoles() { return availableRoles; }
    public void setAvailableRoles(Set<RoleDTO> availableRoles) { this.availableRoles = availableRoles; }
}
