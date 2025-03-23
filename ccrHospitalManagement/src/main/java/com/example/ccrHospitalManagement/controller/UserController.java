package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.service.RoleService;
import com.example.ccrHospitalManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService; // Para cargar roles disponibles en el formulario

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list"; // plantilla: user/list.html
    }

    @GetMapping("/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        // Agregar la lista de roles disponibles
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user/form"; // plantilla: user/form.html
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("roleIds") List<String> roleIds,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "user/form";
        }
        if (userService.getUserById(user.getId()).isPresent()) {
            userService.updateUser(user, roleIds);
        } else {
            userService.createUser(user, roleIds);
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") String id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
