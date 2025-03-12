package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STAFF")
@Data
public class Staff {
    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50)
    private String phone;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "USER_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "CITY_id")
    private City city;

    @Column(length = 50)
    private String address;

    @ManyToOne
    @JoinColumn(name = "PROFESSION_name")
    private Profession profession;
}
