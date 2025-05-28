package com.example.ccrHospitalManagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Data
public class UserRegistrationDto {

    @NotBlank
    private String id;

    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @NotBlank
    @Pattern(regexp = "Hombre|Mujer|Otro", message = "El sexo debe ser 'Hombre', 'Mujer' o 'Otro'")
    private String sex;


    @JsonFormat(pattern = "yyyy-MM-dd") 
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate dateOfBirth;



    @NotBlank
    private String epsNit;

    @NotBlank
    private String prepaidMedicineNit;
}
