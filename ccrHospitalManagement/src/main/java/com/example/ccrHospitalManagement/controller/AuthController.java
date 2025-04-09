package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.service.EpsServiceImpl;
import com.example.ccrHospitalManagement.service.PrepaidMedicineServiceImpl;
import com.example.ccrHospitalManagement.service.UserService;
import com.example.ccrHospitalManagement.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EpsServiceImpl epsServiceImpl;
    private final PrepaidMedicineServiceImpl prepaidMedicineService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        model.addAttribute("epsList", epsServiceImpl.getAllEps());
        model.addAttribute("prepaidList", prepaidMedicineService.getAllPrepaids());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid UserRegistrationDto dto,
                                  BindingResult result,
                                  Model model) {
        model.addAttribute("epsList", epsServiceImpl.getAllEps());
        model.addAttribute("prepaidList", prepaidMedicineService.getAllPrepaids());

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.saveUser(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("customError", e.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered=true";
    }
}
