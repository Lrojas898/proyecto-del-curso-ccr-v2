package com.example.ccrHospitalManagement.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "prebuilt-pages/default-login";
    }


}
