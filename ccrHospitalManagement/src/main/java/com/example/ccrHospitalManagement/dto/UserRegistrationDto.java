package com.example.ccrHospitalManagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

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
    @Pattern(regexp = "Hombre|Mujer", message = "El sexo debe ser 'Hombre' o 'Mujer'")
    private String sex;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotBlank
    private String epsNit;

    @NotBlank
    private String prepaidMedicineNit;
}
