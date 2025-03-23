package com.example.ccrHospitalManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // redirige a /users, por ejemplo
        return "redirect:/users";
    }
}
