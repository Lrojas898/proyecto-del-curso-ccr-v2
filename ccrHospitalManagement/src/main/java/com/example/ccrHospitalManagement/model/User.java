package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MYUSER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String firstName;  // first_name -> firstName

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String sex;

    @Column(nullable = false)
    private java.sql.Date dateOfBirth;

    // Relación con EPS (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "EPS_nit", nullable = false)
    private EPS eps;

    // Relación con PrepaidMedicine (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "PREPAID_MEDICINE_nit", nullable = false)
    private PrepaidMedicine prepaidMedicine;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "MYUSER_ROLE",
            joinColumns = @JoinColumn(name = "MYUSER_id"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointmentsAsPatient = new HashSet<>();

    @OneToMany(mappedBy = "professional")
    private Set<Appointment> appointmentsAsProfessional = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ExamResult> myExamResults = new HashSet<>();

    @OneToMany(mappedBy = "registeredBy")
    private Set<ExamResult> uploadedExamResults = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private ClinicalHistory clinicalHistory;


}
