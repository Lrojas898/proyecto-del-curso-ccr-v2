package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "role/list";
    }

    @GetMapping("/new")
    public String showRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "role/form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "role/form";
        }
        if (roleService.getRoleById(role.getId()).isPresent()) {
            roleService.updateRole(role);
        } else {
            roleService.createRole(role);
        }
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable("id") String id, Model model) {
        Role role = roleService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + id));
        model.addAttribute("role", role);
        return "role/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") String id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
