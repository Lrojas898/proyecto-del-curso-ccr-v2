package com.example.ccrHospitalManagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "USER_ROLE")
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ROLE_name", nullable = false)
    private Role role;
}
