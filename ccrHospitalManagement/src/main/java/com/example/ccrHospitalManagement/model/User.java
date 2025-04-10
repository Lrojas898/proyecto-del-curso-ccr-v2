package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MYUSER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @Column(length = 30, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @Column(length = 100, nullable = false)
    @ToString.Include
    private String username;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    @ToString.Include
    private String firstName;  // first_name -> firstName

    @Column(length = 50, nullable = false)
    @ToString.Include
    private String lastName;

    @Column(length = 50, nullable = false)
    @ToString.Include
    private String address;

    @Column(length = 30, nullable = false)
    @ToString.Include
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

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private Set<Appointment> appointmentsAsPatient = new HashSet<>();

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private Set<Appointment> appointmentsAsProfessional = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ExamResult> myExamResults = new HashSet<>();

    @OneToMany(mappedBy = "registeredBy", fetch = FetchType.LAZY)
    private Set<ExamResult> uploadedExamResults = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private ClinicalHistory clinicalHistory;


}
