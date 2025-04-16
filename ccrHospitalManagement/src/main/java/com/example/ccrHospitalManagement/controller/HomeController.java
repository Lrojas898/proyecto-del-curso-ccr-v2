package com.example.ccrHospitalManagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication != null) {
            String name = authentication.getName();
            model.addAttribute("name", name);
        }
        return "home";
    }



    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
