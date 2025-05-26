package com.example.ccrHospitalManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles; 
}
