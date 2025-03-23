package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.service.PermissionService;
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
    private final PermissionService permissionService; // Inyecta el servicio de permisos

    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "role/list";
    }

    @GetMapping("/new")
    public String showRoleForm(Model model) {
        model.addAttribute("role", new Role());
        // Cargar la lista de permisos disponibles
        model.addAttribute("allPermissions", permissionService.getAllPermissions());
        return "role/form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role,
                           @RequestParam("permissionIds") List<String> permissionIds,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allPermissions", permissionService.getAllPermissions());
            return "role/form";
        }
        if (roleService.getRoleById(role.getId()).isPresent()) {
            roleService.updateRole(role, permissionIds);
        } else {
            roleService.createRole(role, permissionIds);
        }
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable("id") String id, Model model) {
        Role role = roleService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + id));
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", permissionService.getAllPermissions());
        return "role/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") String id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
