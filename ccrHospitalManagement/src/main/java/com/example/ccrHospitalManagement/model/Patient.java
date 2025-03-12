package com.example.ccrHospitalManagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import java.util.Date;



@Entity
@Table(name="PATIENT")
public class Patient {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "sex", length = 5)
    private String sex;

    @Column(name = "blood_type", length = 5)
    private String bloodType;

    @ManyToOne
    @JoinColumn(name = "eps_id")
    private Eps eps;

    @ManyToOne
    @JoinColumn(name = "prepaid_medicine_id")
    private PrepaidMedicine prepaidMedicine;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
}
