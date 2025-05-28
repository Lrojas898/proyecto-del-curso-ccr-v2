package com.example.ccrHospitalManagement.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String sex;
    private LocalDate dateOfBirth;
    private String speciality;
    private String epsName;              
    private String prepaidMedicineName;  
    private List<String> roles;
}
