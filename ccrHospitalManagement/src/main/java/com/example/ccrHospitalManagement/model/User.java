package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MYUSER", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(length = 500, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String sex;

    private LocalDate dateOfBirth;

    private String speciality;

    @ManyToOne
    @JoinColumn(name = "EPS_nit", nullable = false)
    private EPS eps;

    @ManyToOne
    @JoinColumn(name = "PREPAID_MEDICINE_nit", nullable = false)
    private PrepaidMedicine prepaidMedicine;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "MYUSER_id"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_id")
    )
    private Set<Role> roles = new HashSet<>();
}

