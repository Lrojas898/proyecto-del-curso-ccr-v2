package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Role {

    @Id
    @Column(length = 40, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ROLE_id"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_id"))
    private Set<Permission> permissions = new HashSet<>();


}
