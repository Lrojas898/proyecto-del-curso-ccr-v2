package com.example.ccrHospitalManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }



    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
