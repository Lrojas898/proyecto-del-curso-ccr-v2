package com.example.ccrHospitalManagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UserRoleDTO {
    // Getters y setters
    private String userId;
    private String username;
    private Set<String> assignedRoleIds; // estos son los roles que el usuario tiene
    private Set<RoleDTO> availableRoles; // roles disponibles

    public UserRoleDTO(String userId, String username, Set<String> assignedRoleIds, Set<RoleDTO> availableRoles) {
        this.userId = userId;
        this.username = username;
        this.assignedRoleIds = assignedRoleIds;
        this.availableRoles = availableRoles;
    }

}
