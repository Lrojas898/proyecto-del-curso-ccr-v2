package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.UserRoleDTO;
import com.example.ccrHospitalManagement.service.UserService;
import com.example.ccrHospitalManagement.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserServiceImpl userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user-list";
    }
    //continuamos, Dios mediante, por aca.
    @GetMapping("/users/{userId}/roles")
    public String showUserRoles(@PathVariable String userId, Model model) {
        UserRoleDTO userRoleDTO = userService.getUserWithRoles(userId);
        model.addAttribute("userRoleDTO", userRoleDTO);
        return "admin/user-roles";
    }

    @PostMapping("/users/{userId}/roles")
    public String updateUserRoles(@PathVariable String userId, @RequestParam("roleIds") Set<Long> roleIds) {
        userService.updateUserRoles(userId, roleIds);
        return "redirect:/admin/users";
    }
}
