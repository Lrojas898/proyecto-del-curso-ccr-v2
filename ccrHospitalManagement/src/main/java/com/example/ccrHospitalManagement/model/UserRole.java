package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    // Muchas veces se mapean también las referencias directas
    @ManyToOne
    @MapsId("userId") // indica que userId se toma del embebido
    @JoinColumn(name = "USER_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "ROLE_id", nullable = false)
    private Role role;
}
